package com.github.coolcool.sloth.lianjiadb.spider;

import com.github.coolcool.sloth.lianjiadb.model.HttpProxy;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jiang on 2017/9/6.
 */
public class ProxyIPSpider2 implements PageProcessor {

    @Override
    public void process(Page page) {
        List<String> ipList = page.getHtml().xpath("//table[@class='table table-bordered table-striped']/tbody/tr").all();
        List<HttpProxy> result = new ArrayList<>();

        if(ipList != null && ipList.size() > 0){
            for(String tmp : ipList){
                Html html = Html.create(tmp);
                HttpProxy proxyIp = new HttpProxy();
                String[] data = html.xpath("//body/text()").toString().trim().split("\\s+");
                String dataStr = html.xpath("//body/text()").toString();

                proxyIp.setHost(data[0]);
                proxyIp.setPort(Integer.valueOf(data[1]));

                Pattern pattern = Pattern.compile("HTTPS?\\s(.*)?\\s\\d秒");
                Matcher matcher = pattern.matcher(dataStr);
                if(matcher.find()){
                    proxyIp.setLocation(matcher.group(1));
                }
                proxyIp.setStatus(0);
                proxyIp.setCreatetime(new Date());
               // proxyIp.setType(data[3]);

                result.add(proxyIp);
            }
        }
        page.putField("result", result);
        page.addTargetRequest("http://www.kuaidaili.com/free/inha/2/");
        page.addTargetRequest("http://www.kuaidaili.com/free/intr/1/");
    }

    @Override
    public Site getSite() {
        Site site = Site.me().setTimeOut(6000).setRetryTimes(3)
                .setSleepTime(1000).setCharset("UTF-8").addHeader("Accept-Encoding", "/")
                .setUserAgent(UserAgentUtils.radomUserAgent());

        return site;
    }
}
