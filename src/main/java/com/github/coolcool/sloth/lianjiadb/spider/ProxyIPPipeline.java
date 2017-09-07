package com.github.coolcool.sloth.lianjiadb.spider;

import com.github.coolcool.sloth.lianjiadb.mapper.HttpProxyMapper;
import com.github.coolcool.sloth.lianjiadb.model.HttpProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;

/**
 * Created by jiang on 2017/9/6.
 */
@Component
public class ProxyIPPipeline implements Pipeline {
    @Autowired
    HttpProxyMapper httpProxyMapper;

    @Override
    public void process(ResultItems resultItems, Task task) {
        List<HttpProxy> list = resultItems.get("result");

        if (list != null && list.size() > 0) {
            for (HttpProxy proxyIp : list) {
                if (httpProxyMapper.countByHost(proxyIp.getHost()) < 1) {
                    if (CheckIPUtils.checkValidIP(proxyIp.getHost(),proxyIp.getPort())) {
                        httpProxyMapper.save(proxyIp);
                    }

                }
            }
        }

    }
}
