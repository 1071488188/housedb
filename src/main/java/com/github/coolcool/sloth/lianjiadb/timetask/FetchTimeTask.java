package com.github.coolcool.sloth.lianjiadb.timetask;

import com.github.coolcool.sloth.lianjiadb.common.MyHttpClient;
import com.github.coolcool.sloth.lianjiadb.service.ProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.TimerTask;


@EnableScheduling
@Service
public class FetchTimeTask extends TimerTask {

    private static final Logger log = LoggerFactory.getLogger(FetchTimeTask.class);

    @Autowired
    private ProcessService processService;



    static boolean houseUrlsFetching = false;
    static boolean houseDetailFetching = false;
    static boolean genProcessing = false;


    /**
     * 生成当天任务
     */
    @Scheduled(cron="0/30 * *  * * ? ")   //每30秒执行一次
    public void genProcess() {
        if(MyHttpClient.available && !genProcessing){
            genProcessing = true;
            log.info("开始执行genProcessing...");
            try {
                processService.genProcesses();
            }catch (Throwable t){
                t.printStackTrace();
            }
            genProcessing = false;
        }
    }


    /**
     * 根据当天的执行任务，按最小区域（车陂、华景）分页获取房屋链接地址，入库 houseindex
     */
    @Override
    @Scheduled(cron="0/5 * * * * ? ")   //每5秒执行一次
    public void run() {
        if(MyHttpClient.available && !houseUrlsFetching){
            houseUrlsFetching = true;
            log.info("开始执行houseUrlsFetching...");
            try {
                processService.fetchHouseUrls();
            }catch (Throwable t){
                t.printStackTrace();
            }
            houseUrlsFetching = false;
        }
    }

    @Scheduled(cron="0/5 * * * * ? ")   //每5秒执行一次
    public void fetching() {
        if(MyHttpClient.available && !houseDetailFetching){
            houseDetailFetching = true;
            log.info("开始执行houseDetailFetching...");
            try {
                processService.fetchHouseDetail();
            }catch (Throwable t){
                t.printStackTrace();
            }
            houseDetailFetching = false;
        }
    }
    
}
