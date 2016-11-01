package com.github.coolcool.sloth.lianjiadb.common;

import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Random;

/**
 * Created by dee on 2016/11/1.
 */
public abstract class MyHttpClient {

    public static String get(String url){
        return getByRandomProxy(url);
    }

    public static String getByRandomProxy(String url){
        int r = 10;
        Random random = new Random();
        int result = random.nextInt(r);
        if(result<5){
            return get(url, "", 0, 0);
        }else{
            return getByHttpProxy(url,"182.84.98.173", 808);
        }

    }


    public static String get(String url, String proxyHost, int proxyPort, int type){
        StringBuilder sb = new StringBuilder();
        Proxy proxy = null;
        // /创建代理服务器
        if(!StringUtils.isEmpty(proxyHost)) {
            InetSocketAddress addr = new InetSocketAddress(proxyHost, proxyPort);
            if (type == 1)
                proxy = new Proxy(Proxy.Type.SOCKS, addr); // Socket 代理
            else
                proxy = new Proxy(Proxy.Type.HTTP, addr); // http 代理
            Authenticator.setDefault(new MyAuthenticator("te1101", "te1101"));// 设置代理的用户和密码
        }
        HttpURLConnection connection = null;// 设置代理访问
        InputStream is = null;
        try {
            URL tempUrl = new URL(url);
            if(StringUtils.isEmpty(proxyHost)){
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
        }finally {
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if(connection != null){
                connection.disconnect();
            }
        }
        return sb.toString();
    }


    public static String getBySocketProxy(String url, String proxyHost, int proxyPort){
        return get(url, proxyHost, proxyPort, 1);
    }

    public static String getByHttpProxy(String url, String proxyHost, int proxyPort){
        return get(url, proxyHost, proxyPort, 0);
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

}
