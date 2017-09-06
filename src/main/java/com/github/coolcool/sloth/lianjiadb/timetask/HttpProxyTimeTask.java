package com.github.coolcool.sloth.lianjiadb.timetask;

import com.github.coolcool.sloth.lianjiadb.service.CrawlManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jiang on 2017/9/6.
 */
@EnableScheduling
@Service
public class HttpProxyTimeTask {
    @Autowired
    private CrawlManager crawlManager;
    @Scheduled(cron="0 0/1 * * * ?")   //每5分钟执行一次
    public void httpProxy1(){
        Date current = new Date();
        Format format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        crawlManager.proxyIPCrawl();
    }
}
