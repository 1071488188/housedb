package com.github.coolcool.sloth.lianjiadb.timetask;

import com.github.coolcool.sloth.lianjiadb.common.MyHttpClient;
import com.github.coolcool.sloth.lianjiadb.service.ProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.TimerTask;


@EnableScheduling
@Service
public class CheckChangingTimeTask extends TimerTask {

    private static final Logger log = LoggerFactory.getLogger(CheckChangingTimeTask.class);

    @Value("${com.github.coolcool.sloth.lianjiadb.timetask.checkchanging.hour:9}")
    int checkchangingHour;

    @Autowired
    private ProcessService processService;

    static boolean running = false;

    @Override
    @Scheduled(cron="0 0/10 * * * ?")
    public void run() {

        if(LocalTime.now().getHour() != checkchangingHour) // 每天9点执行一次
            return;

        if(MyHttpClient.available && !running){
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
