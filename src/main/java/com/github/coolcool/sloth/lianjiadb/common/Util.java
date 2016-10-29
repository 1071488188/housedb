package com.github.coolcool.sloth.lianjiadb.common;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;

public class Util {
    private static final Logger log = LoggerFactory.getLogger(Util.class);

    //okhttp
    public static String okhttpGet(String url)  {
        //OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", "OkHttp Headers.java")
                //.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                //.addHeader("Accept-Encoding", "gzip, deflate, sdch")
                //.addHeader("Accept-Language", "zh-CN,zh;q=0.8")
                .addHeader("Cache-Control", "max-age=0")
                .addHeader("Connection", "keep-alive")
                //.addHeader("Upgrade-Insecure-Requests","1")
                //.addHeader("Host","gz.lianjia.com")
                .addHeader("Referer", "http://captcha.lianjia.com/?redirect=http%3A%2F%2Fgz.lianjia.com%2Fershoufang%2FGZ0002000813.html")
                .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36")
                .build();
        String result = null;
        try {
            Response response = client.newCall(request).execute();
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static final OkHttpClient client = new OkHttpClient();



}
