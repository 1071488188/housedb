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
public class CheckChangingTimeTask extends TimerTask {

    private static final Logger log = LoggerFactory.getLogger(CheckChangingTimeTask.class);



    @Autowired
    private ProcessService processService;

    static boolean running = false;

    @Override
    @Scheduled(cron="0/15 * * * * ? ")   //每15秒执行一次
    public void run() {
        if(!running){
            running = true;
            log.info("开始执行 checkChanging ...");
            try {
                processService.checkChange();
            }catch (Throwable t){
                t.printStackTrace();
            }
            running = false;
        }
    }

}
