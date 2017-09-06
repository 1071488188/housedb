package com.github.coolcool.sloth.lianjiadb.service;

import org.springframework.stereotype.Component;

/**
 * Created by jiang on 2017/9/6.
 */

public interface CrawlManager {
    /**
     * 代理IP爬虫，地址：http://www.xicidaili.com
     */
    public void proxyIPCrawl();

    /**
     * 代理IP爬虫，地址：http://www.kuaidaili.com
     */
    public void proxyIPCrawl2();
}
