package com.github.coolcool.sloth.lianjiadb.timetask;

import com.alibaba.fastjson.JSONObject;
import com.github.coolcool.sloth.lianjiadb.common.MyHttpClient;
import com.github.coolcool.sloth.lianjiadb.service.ProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.TimerTask;


@EnableScheduling
@Service
public class CheckHttpProxyTask extends TimerTask {

    private static final Logger log = LoggerFactory.getLogger(CheckHttpProxyTask.class);

    static boolean running = false;

    @Override
    @Scheduled(cron="0 0/5 * * * ?")   //每5分钟执行一次
    public void run() {
        if(!running){
            running = true;
            log.info("开始执行 CheckHttpProxyTask ...");
            try {
                String testurl = "http://gz.lianjia.com/ershoufang/GZ0002179878.html";
                List<MyHttpClient.HttpProxyConfig> httpProxyConfigs = MyHttpClient.allHttpProxyConfigs;
                for (int i = 0; i < httpProxyConfigs.size(); i++) {
                    MyHttpClient.HttpProxyConfig httpProxyConfig = httpProxyConfigs.get(i);
                    String result = MyHttpClient.get(testurl,httpProxyConfig);
                    if(result.indexOf("验证异常流量")<0){
                        httpProxyConfig.setStatus(1);
                        log.info("proxyok:"+JSONObject.toJSONString(httpProxyConfig));
                        MyHttpClient.addAvailableHttpProxyConfig(httpProxyConfig);

                    }else{
                        httpProxyConfig.setStatus(0);
                        log.info("proxyerror:"+ JSONObject.toJSONString(httpProxyConfig));
                        MyHttpClient.removeAvailableHttpProxyConfig(httpProxyConfig);
                    }
                }
            }catch (Throwable t){
                t.printStackTrace();
            }
            running = false;
        }
    }

}
