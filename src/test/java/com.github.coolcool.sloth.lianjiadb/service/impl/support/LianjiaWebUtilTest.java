package com.github.coolcool.sloth.lianjiadb.service.impl.support;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by dee on 2017/6/19.
 */
public class LianjiaWebUtilTest {

    static Logger logger = LoggerFactory.getLogger(LianjiaWebUtilTest.class);


    @Test
    public void testFetchAndGenHouseObject(){
        String houseUrl = "https://gz.lianjia.com/ershoufang/GZ0002180546.html";
        logger.info(JSONObject.toJSONString(LianjiaWebUtil.fetchAndGenHouseObject(houseUrl)));
    }

    @Test
    public void testFetchPrice(){
        String houseUrl = "https://gz.lianjia.com/ershoufang/GZ0002180546.html";
        Assert.assertEquals(98,LianjiaWebUtil.fetchPrice(houseUrl).longValue());

    }

    @Test
    public void testFetchHouseHtml(){
        String houseUrl = "https://gz.lianjia.com/ershoufang/GZ0002180546.html";
        String houseHtml = LianjiaWebUtil.fetchHouseHtml(houseUrl);
        logger.info(houseHtml);

    }


    @Test
    public void testFechAreaChenjiaoHouseUrls(){
        String area1 = "tianhe";
        int pageNo = 1;
        Set<String> urls = LianjiaWebUtil.fetchAreaChenjiaoHouseUrls(area1,pageNo);
        Iterator<String> i = urls.iterator();
        while (i.hasNext()){
            logger.info(i.next());
        }

    }


    @Test
    public void testFetchAreaHouseUrls(){
        String area1 = "tianhe";
        int pageNo = 1;
        Set<String> urls = LianjiaWebUtil.fetchAreaHouseUrls(area1,pageNo);
        Iterator<String> i = urls.iterator();
        while (i.hasNext()){
            logger.info(i.next());
        }

    }


    @Test
    public void testFetchAreaChenjiaoTotalPageNo(){
        String area1 = "tianhe";
        logger.info(""+LianjiaWebUtil.fetchAreaChenjiaoTotalPageNo(area1));
    }

    @Test
    public void testFetchAreaTotalPageNo(){
        String area1 = "tianhe";
        logger.info(""+LianjiaWebUtil.fetchAreaTotalPageNo(area1));
    }

    @Test
    public void testGetCityAreaIndexUrl(){
        String city1 = "gz";
        String area1 = "tianhe";
        Assert.assertEquals("https://gz.lianjia.com/ershoufang/tianhe/", LianjiaWebUtil.getCityAreaIndexUrl(city1,area1));
    }

    @Test
    public void testGetCityIndexUrl(){
        String city1 = "gz";
        String city2 = "sh";
        String city3 = "bj";
        Assert.assertEquals("https://gz.lianjia.com/ershoufang/", LianjiaWebUtil.getCityIndexUrl(city1));
        Assert.assertEquals("https://sh.lianjia.com/ershoufang/", LianjiaWebUtil.getCityIndexUrl(city2));
        Assert.assertEquals("https://bj.lianjia.com/ershoufang/", LianjiaWebUtil.getCityIndexUrl(city3));
    }



}
