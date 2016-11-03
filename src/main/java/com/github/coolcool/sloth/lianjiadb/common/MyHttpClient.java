package com.github.coolcool.sloth.lianjiadb.common;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by dee on 2016/11/1.
 */
public abstract class MyHttpClient {

    static Logger logger = LoggerFactory.getLogger(MyHttpClient.class);

    public static int ALLOCATE_VALUE = 10;

    public static  boolean available = false;

    public static List<HttpProxyConfig> allHttpProxyConfigs = new ArrayList<>();
    public static List<HttpProxyConfig> availableHttpProxyConfigs = new ArrayList<>();

    static {
        allHttpProxyConfigs.add(new HttpProxyConfig(1,"",0,"",""));
        allHttpProxyConfigs.add(new HttpProxyConfig(2,"182.84.98.173",808,"te1101","te1101"));
    }
    
    public static void addAvailableHttpProxyConfig(HttpProxyConfig httpProxyConfig){
        for (int i = 0; i < availableHttpProxyConfigs.size(); i++) {
            HttpProxyConfig temp = availableHttpProxyConfigs.get(i);
            if(temp.getId()==httpProxyConfig.getId())
                return ;
        }
        available = true;
        logger.info("set myhttpclient available....");
        availableHttpProxyConfigs.add(httpProxyConfig);
        logger.info("add availableHttpProxyConfigs :"+JSONObject.toJSONString(httpProxyConfig));
    }

    public static void removeAvailableHttpProxyConfig(HttpProxyConfig httpProxyConfig){
        for (int i = 0; i < availableHttpProxyConfigs.size(); i++) {
            HttpProxyConfig temp = availableHttpProxyConfigs.get(i);
            if(temp.getId()==httpProxyConfig.getId()){
                availableHttpProxyConfigs.remove(i);
                logger.info("remove availableHttpProxyConfigs :"+JSONObject.toJSONString(httpProxyConfig));
            }
        }
        if (availableHttpProxyConfigs.size()==0) {
            available = false;
            logger.info("set myhttpclient not available....");
        }
    }


    public static String get(String url){
        return getByRandomProxy(url);
    }

    public static String getByRandomProxy(String url){

        HttpProxyConfig httpProxyConfig = null;

        Random random = new Random();
        int index = random.nextInt(availableHttpProxyConfigs.size());
        for (int i = 0; i < availableHttpProxyConfigs.size(); i++) {
            HttpProxyConfig tempHttpProxyConfig = availableHttpProxyConfigs.get(i);
            if(tempHttpProxyConfig.status==1){
                httpProxyConfig = tempHttpProxyConfig;
                index--;
                if(index<0){
                    break;
                }
            }
        }

        if(httpProxyConfig==null){
            logger.warn("no available httpProxyConfig！");
            try {
                Thread.sleep(60*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "";
        }
        //logger.info("find http proxy :" + JSONObject.toJSONString(httpProxyConfig));
        return get(url, httpProxyConfig);


    }


    public static String get(String url, HttpProxyConfig httpProxyConfig){
        StringBuilder sb = new StringBuilder();
        Proxy proxy = null;
        // /创建代理服务器
        if(!StringUtils.isEmpty(httpProxyConfig.getHost())) {
            InetSocketAddress addr = new InetSocketAddress(httpProxyConfig.getHost(), httpProxyConfig.getPort());
            if (httpProxyConfig.getType() == 1)
                proxy = new Proxy(Proxy.Type.SOCKS, addr); // Socket 代理
            else
                proxy = new Proxy(Proxy.Type.HTTP, addr); // http 代理
            Authenticator.setDefault(new MyAuthenticator(httpProxyConfig.getUsername(), httpProxyConfig.getPassword()));// 设置代理的用户和密码
        }
        HttpURLConnection connection = null;// 设置代理访问
        InputStream is = null;
        try {
            URL tempUrl = new URL(url);
            if(StringUtils.isEmpty(httpProxyConfig.getHost())){
                connection = (HttpURLConnection) tempUrl.openConnection();
            }else {
                connection = (HttpURLConnection) tempUrl.openConnection(proxy);
            }
            is = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);
            String inputLine  = "";
            while ((inputLine = reader.readLine())!=null) {
                sb.append(inputLine).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }finally {
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return "error";
                }
            }
            if(connection != null){
                connection.disconnect();
            }
        }
        return sb.toString();
    }


    static class MyAuthenticator extends Authenticator {
        private String user = "";
        private String password = "";

        public MyAuthenticator(String user, String password) {
            this.user = user;
            this.password = password;
        }

        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(user, password.toCharArray());
        }
    }


    public static class HttpProxyConfig {
        int id;
        String host;
        int port;
        String username;
        String password;
        int status=0; //0:暂停使用；1:使用中
        int type = 0;//0:http proxy; 1:socket proxy

        public HttpProxyConfig(int id,String host, int port, String username, String password) {
            this.id = id;
            this.host = host;
            this.port = port;
            this.username = username;
            this.password = password;
            this.status=1;
            this.type = 0;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

    public static void main(String[] args) {
        String url ="http://gz.lianjia.com/ershoufang/";
        String url2 = "http://gz.lianjia.com/ershoufang/GZ0002180546.html";
        String url3 = "http://gz.lianjia.com/ershoufang/GZ0001565595.html";
        HttpProxyConfig httpProxyConfig = new HttpProxyConfig(2,"182.84.98.173",808,"te1101","te1101");
        String result = get(url2,httpProxyConfig);
        if(result.indexOf("验证异常流量")<0){
            logger.info("OK");
        }else {
            logger.info("er");
        }
    }

}
