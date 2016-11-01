package com.github.coolcool.sloth.lianjiadb.service.impl.support;

import com.alibaba.fastjson.JSONObject;
import com.github.coolcool.sloth.lianjiadb.common.Util;
import com.github.coolcool.sloth.lianjiadb.model.House;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dee on 2016/10/19.
 */
public abstract class LianjiaWebUtil {

    static Logger logger = LoggerFactory.getLogger(LianjiaWebUtil.class);

    static String cityIndexUrl = "http://${city}.lianjia.com/ershoufang/";
    static String cityAreaIndexUrl = "http://${city}.lianjia.com/ershoufang/${area}/";

    static String firstPageAreasUrl = "http://gz.lianjia.com/ershoufang/${area}/";
    static String pageAreasUrl = "http://gz.lianjia.com/ershoufang/${area}/pg${pageNo}/";



    //解析正则
    static Matcher matcher = null;
    static Pattern houseUrlInPageWebPattern = Pattern.compile("http://gz.lianjia.com/ershoufang/GZ[0-9]+.html");
    static Pattern totalPageNoInPageWebPattern = Pattern.compile("\"totalPage\":([0-9]+)");
    //详细信息
    static Pattern titlePattern = Pattern.compile("class=\"main\" title=\"(.*?)\"");
    static Pattern subTitlePattern = Pattern.compile("class=\"sub\" title=\"(.*?)\"");

    static Pattern favCountPattern = Pattern.compile("id=\"favCount\" class=\"count\">([0-9]+)<");
    static Pattern cartCountPattern = Pattern.compile("id=\"cartCount\" class=\"count\">([0-9]+)<");

    static Pattern pricePattern = Pattern.compile("span class=\"total\">([0-9]+(.[0-9]?))</span>");
    static Pattern unitPricePattern = Pattern.compile("class=\"unitPriceValue\">([0-9]+(.[0-9]?))<");

    static Pattern firstPayPricePattern = Pattern.compile("首付([0-9]+(.[0-9]?))万");
    static Pattern taxPricePattern = Pattern.compile("id=\"PanelTax\">([0-9]+(.[0-9]?))<");
    static Pattern removedPattern = Pattern.compile("<span>已下架</span>");

    static Pattern roomMainAndSubInfoPattern = Pattern.compile("div class=\"room\"><div class=\"mainInfo\">(.*?)</div><div class=\"subInfo\">(.*?)</div></div>");
    static Pattern roomMainAndSubTypePattern = Pattern.compile("<div class=\"type\"><div class=\"mainInfo\" title=\".*?\">(.*?)</div><div class=\"subInfo\">(.*?)</div></div>");
    static Pattern areaMainAndSubInfoPattern = Pattern.compile("<div class=\"area\"><div class=\"mainInfo\">(.*?)</div><div class=\"subInfo\">(.*?)</div></div>");
    static Pattern communityNamePattern = Pattern.compile("<div class=\"communityName\"><i></i><span class=\"label\">.*?><a.*?>(.*?)</a>");
    static Pattern areaNameHtmlPattern = Pattern.compile("<div class=\"areaName\"><i></i><span class=\"label\">所在区域</span><span class=\"info\">(.*?)</span>");
    static Pattern areaNameHtmlPattern_1 = Pattern.compile("<a.*?>(.*?)</a>");
    static Pattern schoolNamePattern = Pattern.compile("<div class=\"schoolName\"><i class=\"icon\"></i><span class=\"label\">对口学校</span><span class=\"info\"><a.*?>(.*?)<");
    static Pattern baseContentPattern = Pattern.compile("<div class=\"base\"><div class=\"name\">基本属性</div><div class=\"content\"><ul><li><span class=\"label\">房屋户型</span>(.*?)</li><li><span class=\"label\">所在楼层</span>(.*?)</li><li><span class=\"label\">建筑面积</span>(.*?)</li><li><span class=\"label\">户型结构</span>(.*?)</li><li><span class=\"label\">套内面积</span>(.*?)</li><li><span class=\"label\">建筑类型</span>(.*?)</li><li><span class=\"label\">房屋朝向</span>(.*?)</li><li><span class=\"label\">建筑结构</span>(.*?)</li><li><span class=\"label\">装修情况</span>(.*?)</li><li><span class=\"label\">梯户比例</span>(.*?)</li><li><span class=\"label\">供暖方式</span>(.*?)</li><li><span class=\"label\">配备电梯</span>(.*?)</li></ul></div></div>");
    static Pattern transactionContentPattern = Pattern.compile("<div class=\"transaction\"><div class=\"name\">交易属性</div><div class=\"content\"><ul><li><span class=\"label\">挂牌时间</span>(.*?)</li><li><span class=\"label\">交易权属</span>(.*?)</li><li><span class=\"label\">上次交易</span>(.*?)</li><li><span class=\"label\">房屋用途</span>(.*?)</li><li><span class=\"label\">房本年限</span>(.*?)</li><li><span class=\"label\">产权所属</span>(.*?)</li><li><span class=\"label\">是否唯一</span>(.*?)</li><li><span class=\"label\">小区类型</span>(.*?)</li><li><span class=\"label\">抵押信息</span>(.*?)</li><li><span class=\"label\">房本备件</span>(.*?)</li></ul></div></div>");
    static Pattern tagsHtmlPattern = Pattern.compile("<div class=\"name\">房源标签</div><div class=\"content\">(.*?)</div>");
    static Pattern tagsHtmlPattern_1 = Pattern.compile("<a class=\"tag\".*?>(.*?)</a>");
    static Pattern decoratingDescPattern = Pattern.compile("<div class=\"name\">装修描述</div><div class=\"content\">((.|\n|\r)*?)</div>");
    static Pattern houseTypeDescPattern = Pattern.compile("<div class=\"name\">户型介绍</div><div class=\"content\">((.|\n|\r)*?)</div>");
    static Pattern investmentDescPattern = Pattern.compile("<div class=\"name\">投资分析</div><div class=\"content\">((.|\n|\r)*?)</div>");
    static Pattern villageDescPattern = Pattern.compile("<div class=\"name\">小区介绍</div><div class=\"content\">((.|\n|\r)*?)</div>");
    static Pattern schoolDescPattern = Pattern.compile("<div class=\"name\">学校介绍</div><div class=\"content\">((.|\n|\r)*?)</div>");
    static Pattern sellingPointDescPattern = Pattern.compile("<div class=\"name\">核心卖点</div><div class=\"content\">((.|\n|\r)*?)</div>");
    static Pattern reason4saleDescPattern = Pattern.compile("<div class=\"name\">售房原因</div><div class=\"content\">((.|\n|\r)*?)</div>");
    static Pattern supportingDescPattern = Pattern.compile("<div class=\"name\">周边配套</div><div class=\"content\">((.|\n|\r)*?)</div>");
    static Pattern trafficDescPattern = Pattern.compile("<div class=\"name\">交通出行</div><div class=\"content\">((.|\n|\r)*?)</div>");
    static Pattern Pattern172 = Pattern.compile("");
    static Pattern Pattern182 = Pattern.compile("");
    static Pattern Pattern192 = Pattern.compile("");
    static Pattern Pattern102 = Pattern.compile("");
    static Pattern Pattern11 = Pattern.compile("");
    static Pattern Pattern126 = Pattern.compile("");
    static Pattern Pattern127 = Pattern.compile("");
    static Pattern Pattern128 = Pattern.compile("");
    static Pattern Pattern129 = Pattern.compile("");


    /**
     * 二手房首页
     * @param city
     * @return
     */
    public static String getCityIndexUrl(String city){
        return cityIndexUrl.replace("${city}",city);
    }

    public static String getCityAreaIndexUrl(String city, String area){
        return cityAreaIndexUrl.replace("${city}",city).replace("${area}",area);
    }

    public static int fetchAreaTotalPageNo(String area){
        String pageUrl = firstPageAreasUrl.replace("${area}", area);
        String result = Util.okhttpGet(pageUrl);
        Matcher matcher = totalPageNoInPageWebPattern.matcher(result);
        while (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return 0;
    }

    public static Set<String> fetchAreaHouseUrls(String area, int pageNo) {

        Set<String> urls = new HashSet<>();

        System.out.println("start fetching area=" + area + ",pageNo="+pageNo);
        String pageUrl = null;

            String result = "";
            if (pageNo == 1) {
                pageUrl = firstPageAreasUrl.replace("${area}", area);
                result = Util.okhttpGet(pageUrl);
            } else {
                pageUrl = pageAreasUrl.replace("${area}", area).replace("${pageNo}", pageNo + "");
                result = Util.okhttpGet(pageUrl);
            }
            Matcher matcher = houseUrlInPageWebPattern.matcher(result);
            while (matcher.find()) {
                String fangUrl = matcher.group();
                logger.info(fangUrl);
                urls.add(fangUrl);
            }
        logger.info("size:"+urls.size());
        return urls;
    }


    public static BigDecimal fetchPrice(String houseUrl){
        BigDecimal bigDecimal = null;
        String fangUrl = houseUrl;
        String result = Util.okhttpGet(fangUrl);
        //result = result.replace("\r","").replace("\n","").replaceAll(">(.*?)<","");
        String reg = ">\\s+([^\\s<]*)\\s+<";
        result = result.replaceAll(reg, ">$1<");
        matcher = pricePattern.matcher(result);
        if(matcher.find()) {
            bigDecimal = new BigDecimal(matcher.group(1));
        }
        return bigDecimal;
    }

    public static String fetchHouseHtml(String houseUrl){
        String result = Util.okhttpGet(houseUrl);
        String reg = ">\\s+([^\\s<]*)\\s+<";
        result = result.replaceAll(reg, ">$1<");
        return result;
    }

    public static boolean fetchRemoved(String houseUrl){
        String fangUrl = houseUrl;
        return getRemoved(Util.okhttpGet(fangUrl));
    }

    public static boolean getRemoved(String houseHtml){
        String result = houseHtml;
        String reg = ">\\s+([^\\s<]*)\\s+<";
        result = result.replaceAll(reg, ">$1<");
        matcher = removedPattern.matcher(result);
        if(matcher.find()) {
            return true;
        }
        return false;
    }




    public static House fetchAndGenHouseObject(String houseUrl) {
        House house = new House(houseUrl);
        String fangUrl = houseUrl;
        String result = Util.okhttpGet(fangUrl);
        //result = result.replace("\r","").replace("\n","").replaceAll(">(.*?)<","");
        String reg = ">\\s+([^\\s<]*)\\s+<";
        result = result.replaceAll(reg, ">$1<");
        //System.out.println(result);

        house.setHtml(result);

        matcher = titlePattern.matcher(result);
        if(matcher.find()) {
            house.setTitle(matcher.group(1));
        }

        matcher = subTitlePattern.matcher(result);
        if(matcher.find()) {
            house.setSubtitle(matcher.group(1));
        }

        matcher = favCountPattern.matcher(result);
        if(matcher.find()) {
            house.setFavcount(Integer.parseInt(matcher.group(1)));
        }

        matcher = cartCountPattern.matcher(result);
        if(matcher.find()) {
            house.setCartcount(Integer.parseInt(matcher.group(1)));
        }

        matcher = pricePattern.matcher(result);
        if(matcher.find()) {
            house.setPrice(new BigDecimal(matcher.group(1)));
        }

        matcher = unitPricePattern.matcher(result);
        if(matcher.find()) {
            house.setUnitprice(new BigDecimal(matcher.group(1)));
        }

        matcher = firstPayPricePattern.matcher(result);
        if(matcher.find()) {
            house.setFirstPayPrice(new BigDecimal(matcher.group(1)));
        }

        matcher = taxPricePattern.matcher(result);
        if(matcher.find()) {
            house.setTaxPrice(new BigDecimal(matcher.group(1)));
        }

        matcher = roomMainAndSubInfoPattern.matcher(result);
        if(matcher.find()) {
            house.setRoomMainInfo(matcher.group(1));
            house.setRoomSubInfo(matcher.group(2));
        }

        matcher = roomMainAndSubTypePattern.matcher(result);
        if(matcher.find()) {
            house.setRoomMainType(matcher.group(1));
            house.setRoomSubType(matcher.group(2));
        }


        matcher = areaMainAndSubInfoPattern.matcher(result);
        if(matcher.find()) {
            house.setAreaMainInfo(matcher.group(1));
            house.setAreaSubInfo(matcher.group(2));
        }

        matcher = communityNamePattern.matcher(result);
        if(matcher.find()) {
            house.setCommunityName(matcher.group(1));
        }

        matcher = areaNameHtmlPattern.matcher(result);
        if(matcher.find()) {
            String r = matcher.group(1);
            Matcher tmatcher = areaNameHtmlPattern_1.matcher(r);
            StringBuffer sb = new StringBuffer();
            while (tmatcher.find()){
                sb.append(" ").append(tmatcher.group(1));
            }
            house.setAreaName(sb.toString());
        }

        matcher = schoolNamePattern.matcher(result);
        if(matcher.find()) {
            house.setSchoolName(matcher.group(1));
        }

        matcher = baseContentPattern.matcher(result);
        if(matcher.find()) {
            house.setBaseContent1(matcher.group(1));
            house.setBaseContent2(matcher.group(2));
            house.setBaseContent3(matcher.group(3));
            house.setBaseContent4(matcher.group(4));
            house.setBaseContent5(matcher.group(5));
            house.setBaseContent6(matcher.group(6));
            house.setBaseContent7(matcher.group(7));
            house.setBaseContent8(matcher.group(8));
            house.setBaseContent9(matcher.group(9));
            house.setBaseContent10(matcher.group(10));
            house.setBaseContent11(matcher.group(11));
            house.setBaseContent12(matcher.group(12));
        }

        matcher = transactionContentPattern.matcher(result);
        if(matcher.find()) {
            house.setTransactionContent1(matcher.group(1));
            house.setTransactionContent2(matcher.group(2));
            house.setTransactionContent3(matcher.group(3));
            house.setTransactionContent4(matcher.group(4));
            house.setTransactionContent5(matcher.group(5));
            house.setTransactionContent6(matcher.group(6));
            house.setTransactionContent7(matcher.group(7));
            house.setTransactionContent8(matcher.group(8));
            house.setTransactionContent9(matcher.group(9));
            house.setTransactionContent10(matcher.group(10));
            //System.out.println(matcher.group(11));
            //System.out.println(matcher.group(12));

        }


        matcher = tagsHtmlPattern.matcher(result);
        if(matcher.find()) {
            String tagsHtml = matcher.group(1);
            Matcher tmatcher = tagsHtmlPattern_1.matcher(tagsHtml);
            StringBuffer tagsb = new StringBuffer();
            while ((tmatcher.find())){
                tagsb.append(" ").append(tmatcher.group(1));
            }
            house.setTags(tagsb.toString());
        }

        matcher = decoratingDescPattern.matcher(result);
        if(matcher.find()) {
            house.setDecoratingDesc(matcher.group(1).replace("  ","").replaceAll("\n|\r",""));
        }

        matcher = houseTypeDescPattern.matcher(result);
        if(matcher.find()) {
            house.setHouseTypeDesc(matcher.group(1).replace("  ","").replaceAll("\n|\r",""));
        }

        matcher = investmentDescPattern.matcher(result);
        if(matcher.find()) {
            house.setInvestmentDesc(matcher.group(1).replace("  ","").replaceAll("\n|\r",""));
        }

        matcher = villageDescPattern.matcher(result);
        if(matcher.find()) {
            house.setVillageDesc(matcher.group(1).replace("  ","").replaceAll("\n|\r",""));
        }

        matcher = schoolDescPattern.matcher(result);
        if(matcher.find()) {
            house.setSchoolDesc(matcher.group(1).replace("  ","").replaceAll("\n|\r",""));
        }

        matcher = sellingPointDescPattern.matcher(result);
        if(matcher.find()) {
            house.setSellingPointDesc(matcher.group(1).replace("  ","").replaceAll("\n|\r",""));
        }


        matcher = reason4saleDescPattern.matcher(result);
        if(matcher.find()) {
            house.setReason4saleDesc(matcher.group(1).replace("  ","").replaceAll("\n|\r",""));
        }


        matcher = supportingDescPattern.matcher(result);
        if(matcher.find()) {
            house.setSupportingDesc(matcher.group(1).replace("  ","").replaceAll("\n|\r",""));
        }

        matcher = trafficDescPattern.matcher(result);
        if(matcher.find()) {
            house.setTrafficDesc(matcher.group(1).replace("  ","").replaceAll("\n|\r",""));
        }

        return house;
    }


    /**
     *生成各个城市的区域sql
     * 上海需要特殊处理
     */
    static public void fetchArea(){
        String city = "bj";
        String name = "北京";
        String cityId = "1";
        String basesql = "insert into area ( `name`,`code`,`parentsId`) values ('${name}','${code}',${cityId});";
        String cityIndexUr = getCityIndexUrl(city);
        String result = Util.okhttpGet(cityIndexUr);
        String reg = ">\\s+([^\\s<]*)\\s+<";
        result = result.replaceAll(reg, ">$1<");
        //System.out.println(result);
        Pattern tempPattern = Pattern.compile("<div data-role=\"ershoufang\" ><div>(.*?)</div></div>");
        Matcher matcher = tempPattern.matcher(result);
        String tempResult = "";
        if (matcher.find()) {
            tempResult = matcher.group(1);
        }
        tempPattern = Pattern.compile("<a href=\"/ershoufang/(.*?)/\"  title=\""+name+"(.*?)在售二手房 \">");
        matcher = tempPattern.matcher(tempResult);
        while (matcher.find()) {
            System.out.println(basesql.replace("${code}",matcher.group(1)).replace("${name}",matcher.group(2)).replace("${cityId}",cityId) );
        }
    }





    public static void main(String[] args) {

        String city = "gz";
        String area = "yuexiu";
        String cityId = "3";
        //String areaName = "天河";
        String areaId = "48";

        String basesql = "insert into area ( `name`,`code`,`parentsId`) values ('${name}','${code}',${cityId});";
        String cityAreaIndexUr = getCityAreaIndexUrl(city,area);
        String result = Util.okhttpGet(cityAreaIndexUr);
        String reg = ">\\s+([^\\s<]*)\\s+<";
        result = result.replaceAll(reg, ">$1<");
        //System.out.println(result);
        Pattern tempPattern = Pattern.compile("<div data-role=\"ershoufang\" ><div>(.*?)</div></div><!-- 地铁 -->");
        Matcher matcher = tempPattern.matcher(result);
        String tempResult = "";
        if (matcher.find()) {
            tempResult = matcher.group(1);
        }
        //System.out.println(tempResult);
        tempPattern = Pattern.compile("<a href=\"/ershoufang/(.*?)/\" >(.*?)</a>");
        matcher = tempPattern.matcher(tempResult);
        while (matcher.find()) {
            System.out.println(basesql.replace("${code}",matcher.group(1)).replace("${name}",matcher.group(2)).replace("${cityId}",areaId) );
        }

    }


}
