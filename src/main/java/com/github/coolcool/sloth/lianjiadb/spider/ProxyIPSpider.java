package com.github.coolcool.sloth.lianjiadb.spider;

import com.github.coolcool.sloth.lianjiadb.model.HttpProxy;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jiang on 2017/9/6.
 */
public class ProxyIPSpider implements PageProcessor {
    @Override
    public void process(Page page) {
        List<String> ipList = page.getHtml().xpath("//table[@id='ip_list']/tbody/tr").all();
        List<HttpProxy> result = new ArrayList<>();

        if(ipList != null && ipList.size() > 0){
            ipList.remove(0);  //移除表头
            for(String tmp : ipList){
                Html html = Html.create(tmp);
                HttpProxy proxyIp = new HttpProxy();
                String[] data = html.xpath("//body/text()").toString().trim().split("\\s+");
                proxyIp.setHost(data[0]);
                proxyIp.setPort(Integer.valueOf(data[1]));
                proxyIp.setLocation(html.xpath("//a/text()").toString());
               // proxyIp.setType(data[3]);
                proxyIp.setStatus(0);
                proxyIp.setCreatetime(new Date());

                result.add(proxyIp);
            }
        }
        page.putField("result", result);
        page.addTargetRequest("http://www.xicidaili.com/nn/2");
        page.addTargetRequest("http://www.xicidaili.com/nt/");
    }

    @Override
    public Site getSite() {
        Site site = Site.me().setTimeOut(6000).setRetryTimes(3)
                .setSleepTime(1000).setCharset("UTF-8").addHeader("Accept-Encoding", "/")
                .setUserAgent(UserAgentUtils.radomUserAgent());
        return site;
    }
}
