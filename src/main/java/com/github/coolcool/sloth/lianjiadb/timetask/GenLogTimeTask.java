package com.github.coolcool.sloth.lianjiadb.timetask;

import com.github.coolcool.sloth.lianjiadb.common.Page;
import com.github.coolcool.sloth.lianjiadb.common.log.HouseLog;
import com.github.coolcool.sloth.lianjiadb.common.log.LogstashUtil;
import com.github.coolcool.sloth.lianjiadb.model.House;
import com.github.coolcool.sloth.lianjiadb.service.HouseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.TimerTask;

/**
 * Created by dee on 2016/11/17.
 */
@EnableScheduling
@Service
public class GenLogTimeTask extends TimerTask {

    private static final Logger logstash = LoggerFactory.getLogger(LogstashUtil.class);
    private static final Logger log = LoggerFactory.getLogger(GenLogTimeTask.class);

    static boolean finished = false;

    @Autowired
    private HouseService houseService;

    @Override
    @Scheduled(cron="0 0 23 * * ?")
    public void run() {

        log.info("开始执行 GenLogTimeTask...");

        int pageNo = 1;
        int pageSize = 500;

        while (!finished) {
            Page<House> housePage = houseService.page(pageNo, pageSize);
            if(housePage==null || housePage.getResult()==null || housePage.getResult().size()==0)
                break;
            List<House> houses = housePage.getResult();
            for (int i = 0; i < houses.size(); i++) {
                House house = houses.get(i);
                if(house==null || house.getTitle() == null)
                    continue;
                HouseLog houseLog = new HouseLog(house);
                logstash.info(houseLog.toLogString());
            }
            pageNo++;
        }

        finished = true;

        logstash.info("finished................");

    }
}
