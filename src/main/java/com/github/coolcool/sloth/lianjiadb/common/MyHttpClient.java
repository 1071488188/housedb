package com.github.coolcool.sloth.lianjiadb.common;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.*;
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

    public static List<HttpProxyConfig> allHttpProxyConfigs = new ArrayList<>();//所有的
    public static List<HttpProxyConfig> availableHttpProxyConfigs = new ArrayList<>();//目前可用

    static {
        allHttpProxyConfigs.add(new HttpProxyConfig("",0,"",""));
        availableHttpProxyConfigs.add(new HttpProxyConfig("",0,"",""));
    }
    
    public static void addAvailableHttpProxyConfig(HttpProxyConfig httpProxyConfig){
        available = true;
        for (int i = 0; i < availableHttpProxyConfigs.size(); i++) {
            HttpProxyConfig temp = availableHttpProxyConfigs.get(i);
            if(temp.getHost().equals(httpProxyConfig.getHost()))
                return ;
        }
        logger.info("set myhttpclient available....");
        availableHttpProxyConfigs.add(httpProxyConfig);
        logger.info("add available HttpProxyConfigs :"+JSONObject.toJSONString(httpProxyConfig));
    }

    public static void removeAvailableHttpProxyConfig(HttpProxyConfig httpProxyConfig){
        for (int i = 0; i < availableHttpProxyConfigs.size(); i++) {
            HttpProxyConfig temp = availableHttpProxyConfigs.get(i);
            if(temp.getHost().equals(httpProxyConfig.getHost())){
                availableHttpProxyConfigs.remove(i);
                logger.info("remove available HttpProxyConfigs :"+JSONObject.toJSONString(httpProxyConfig));
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
        if(httpProxyConfig!=null && !StringUtils.isEmpty(httpProxyConfig.getHost())) {
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
            if(httpProxyConfig==null || StringUtils.isEmpty(httpProxyConfig.getHost())){
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
        String host;
        int port;
        String username;
        String password;
        int status=0; //0:暂停使用；1:使用中
        int type = 0;//0:http proxy; 1:socket proxy

        public HttpProxyConfig(){

        }

        public HttpProxyConfig(String host, int port, String username, String password) {
            this.host = host;
            this.port = port;
            this.username = username;
            this.password = password;
            this.status=1;
            this.type = 0;
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

    public static void download(String urlString, String filename) throws Exception {

        URL url = new URL(urlString); // 构造URL
        URLConnection con = url.openConnection();  // 打开链接
        con.setConnectTimeout(5*1000);  //设置请求超时为5s
        InputStream is = con.getInputStream();  // 输入流
        byte[] bs = new byte[1024];  // 1K的数据缓冲
        int len;  // 读取到的数据长度
        int i = filename.length();
        for(i--;i>=0 && filename.charAt(i) != '\\' && filename.charAt(i) != '/';i--);
        String s_dir = filename.substring(0, i);
        File dir = new File(s_dir);  // 输出的文件流
        if(!dir.exists()){
            dir.mkdirs();
        }
        OutputStream os = new FileOutputStream(filename);
        // 开始读取
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        // 完毕，关闭所有链接
        os.close();
        is.close();
    }





    public static void main(String[] args) {
        String url ="https://gz.lianjia.com/ershoufang/";
        String url2 = "https://gz.lianjia.com/ershoufang/GZ0002180546.html";
        String url3 = "https://gz.lianjia.com/ershoufang/GZ0001565595.html";
        HttpProxyConfig httpProxyConfig = new HttpProxyConfig("",0,"","");
//        HttpProxyConfig httpProxyConfig = new HttpProxyConfig("123.59.12.81",10041,"","");
//        HttpProxyConfig httpProxyConfig = new HttpProxyConfig("123.59.12.81",10041,"","");
//        HttpProxyConfig httpProxyConfig = new HttpProxyConfig("123.59.12.81",10041,"","");
//        HttpProxyConfig httpProxyConfig = new HttpProxyConfig("123.59.12.81",10041,"","");
//        HttpProxyConfig httpProxyConfig = new HttpProxyConfig("123.59.12.81",10041,"","");
//        HttpProxyConfig httpProxyConfig = new HttpProxyConfig("123.59.12.81",10041,"","");
        String result = get(url2,httpProxyConfig);
        if( (result.indexOf("验证异常流量")>-1) || (result.indexOf("ERROR.TIP_TITLE")>-1) ){
            logger.info("er");
        }else if ("error".equals(result)){
            logger.info("er");
        }else {
            logger.info("OK");
            logger.info(result);
        }
    }

}
