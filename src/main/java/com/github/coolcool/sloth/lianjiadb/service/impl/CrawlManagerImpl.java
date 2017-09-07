package com.github.coolcool.sloth.lianjiadb.service.impl;

import com.github.coolcool.sloth.lianjiadb.service.CrawlManager;
import com.github.coolcool.sloth.lianjiadb.spider.ProxyIPPipeline;
import com.github.coolcool.sloth.lianjiadb.spider.ProxyIPSpider;
import com.github.coolcool.sloth.lianjiadb.spider.ProxyIPSpider2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.model.OOSpider;

/**
 * Created by jiang on 2017/9/6.
 */
@Service
public class CrawlManagerImpl implements CrawlManager {
    @Autowired
    private ProxyIPPipeline proxyIPPipeline;
    @Override
    public void proxyIPCrawl() {
        OOSpider.create(new ProxyIPSpider())
                .addUrl("http://www.xicidaili.com/nn/1").addPipeline(proxyIPPipeline)
                .thread(3)
                .run();
    }

    @Override
    public void proxyIPCrawl2() {

        OOSpider.create(new ProxyIPSpider2())
                .addUrl("http://www.kuaidaili.com/free/inha/1/").addPipeline(proxyIPPipeline)
                .thread(2)
                .run();
    }
}
