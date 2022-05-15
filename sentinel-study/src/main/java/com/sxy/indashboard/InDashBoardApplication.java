package com.sxy.indashboard;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * @PACKAGE_NAME: com.sxy.indashboard
 * @NAME: InDashBoardApplication
 * @USER: sxy
 * @DATE: 2022/5/11
 * @PROJECT_NAME: Sentinel-sxy
 */
@SpringBootApplication
public class InDashBoardApplication {

    public static void initFlowRule(){
        List<FlowRule> flowRules = new ArrayList<>();
        FlowRule rule = new FlowRule();
        //注意：规则一定要绑定到对应的资源上，通过资源名进行绑定
        rule.setResource("helloworld");
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule.setCount(20);
//        rule.setLimitApp()
//        rule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_WARM_UP);
        flowRules.add(rule);
        //规则管理器
        FlowRuleManager.loadRules(flowRules);
    }

    public static void main(String[] args) {
        SpringApplication.run(InDashBoardApplication.class, args);
        initFlowRule();
        System.out.println("加载配置完毕");
    }
}
