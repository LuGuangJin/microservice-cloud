package tech.jabari.product.config;


import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Collections;

@Configuration
public class ProductSentinelConfig {


    // 2. 热点规则配置
    @PostConstruct
    public void initParamFlowRules() {
        ParamFlowRule rule = new ParamFlowRule("getProduct")
                .setParamIdx(0)  // 对第0个参数（id）限流
                .setCount(5);    // 单机阈值：5次/秒
        ParamFlowRuleManager.loadRules(Collections.singletonList(rule));
    }


}
