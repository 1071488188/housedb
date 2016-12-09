package com.github.coolcool.sloth.lianjiadb.timetask;

import com.alibaba.fastjson.JSONObject;
import com.github.coolcool.sloth.lianjiadb.common.MyHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.TimerTask;


@EnableScheduling
@Service
public class CheckHttpProxyTimeTask extends TimerTask {

    private static final Logger log = LoggerFactory.getLogger(CheckHttpProxyTimeTask.class);

    static boolean running = false;

    @Override
    @Scheduled(cron="0 0/1 * * * ?")   //每15分钟执行一次
    public void run() {
        if(!running){
            running = true;
            try {
                String testurl = "http://gz.lianjia.com/ershoufang/GZ0002179878.html";
                for (int i = 0; i < MyHttpClient.allHttpProxyConfigs.size(); i++) {
                    MyHttpClient.HttpProxyConfig httpProxyConfig = MyHttpClient.allHttpProxyConfigs.get(i);
                    String result = MyHttpClient.get(testurl,httpProxyConfig);
                    if("error".equals(result) || result.indexOf("验证异常流量")>-1){
                        httpProxyConfig.setStatus(0);
                        //log.info("proxyerror:"+ JSONObject.toJSONString(httpProxyConfig));
                        MyHttpClient.removeAvailableHttpProxyConfig(httpProxyConfig);
                    }else {
                        httpProxyConfig.setStatus(1);
                        //log.info("proxyok:"+JSONObject.toJSONString(httpProxyConfig));
                        MyHttpClient.addAvailableHttpProxyConfig(httpProxyConfig);
                    }
                }
            }catch (Throwable t){
                t.printStackTrace();
            }

            //判断目前可用的代理数量
            if(MyHttpClient.allHttpProxyConfigs.size()<=0){
                //log.info("开始执行 CheckHttpProxyTask ...");
                String urlStr = "http://dev.kuaidaili.com/api/getproxy/?orderid=908098272185542&num=30&area=中国&an_tr=1&f_loc=1&f_an=1";
                String str = "";
                try {
                    URL url = new URL(urlStr);
                    InputStream is = url.openStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);
                    while((str = br.readLine()) != null) {
                        System.out.println(str);
                        String[] aa = str.split(",");
                        if(aa.length<2){
                            log.warn(" fetch done... "+str);
                            break;
                        }
                        String[] temp = aa[0].split(":");
                        MyHttpClient.HttpProxyConfig httpProxyConfig = new MyHttpClient.HttpProxyConfig();
                        httpProxyConfig.setHost(temp[0]);
                        httpProxyConfig.setPort(Integer.parseInt(temp[1]));
                        MyHttpClient.addAvailableHttpProxyConfig(httpProxyConfig);
                    }
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            running = false;
        }
    }

}
