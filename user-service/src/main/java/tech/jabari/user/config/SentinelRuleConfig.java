package tech.jabari.user.config;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

// 初始化熔断规则（项目启动时执行）
@Configuration
public class SentinelRuleConfig {
    @PostConstruct
    public void initDegradeRules() {
        List<DegradeRule> rules = new ArrayList<>();
        /*
            Sentinel的熔断规则：资源是 "getUserInfo"，基于响应时间（RT）熔断，阈值 200ms，
            熔断时长 10 秒，最小请求数 5，统计时间窗口 1000ms（1 秒）。
            这意味着，在 1 秒内，如果有至少 5 个请求，
            且这些请求的平均 RT 超过 200ms，就会触发熔断，接下来 10 秒内的请求会被熔断。
         */
        DegradeRule rule = new DegradeRule("getUserInfo")
                .setGrade(RuleConstant.DEGRADE_GRADE_RT) // 基于响应时间熔断
                .setCount(200)  // 响应时间阈值(ms)
                .setTimeWindow(10) // 熔断时长10秒
                .setMinRequestAmount(5) // 最小请求数（触发熔断的前提）
                .setStatIntervalMs(1000); // 统计时间窗口（毫秒）
        rules.add(rule);
        DegradeRuleManager.loadRules(rules);
    }
}