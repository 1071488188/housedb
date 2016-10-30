package com.github.coolcool.sloth.lianjiadb.timetask;

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
public class TimeTask extends TimerTask {

    private static final Logger log = LoggerFactory.getLogger(TimeTask.class);



    @Autowired
    private ProcessService processService;

    static boolean houseUrlsFetching = false;
    static boolean houseDetailFetching = false;
    static boolean genProcessing = false;

    @Override
    @Scheduled(cron="0/5 * *  * * ? ")   //每5秒执行一次
    public void run() {
        if(!houseUrlsFetching){
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


    //@Scheduled(cron="0/5 * *  * * ? ")   //每5秒执行一次
    public void fetching() {
        if(!houseDetailFetching){
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


    /**
     * 生成任务
     */
    @Scheduled(cron="0/60 * *  * * ? ")   //每5秒执行一次
    public void genProcess() {
        if(!genProcessing){
            genProcessing = true;
            log.info("genProcessing...");
            try {
                processService.genProcesses();
            }catch (Throwable t){
                t.printStackTrace();
            }
            genProcessing = false;
        }
    }
    
}
