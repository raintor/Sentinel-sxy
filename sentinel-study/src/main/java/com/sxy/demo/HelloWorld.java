package com.sxy.demo;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @PACKAGE_NAME: com.sxy.demo
 * @NAME: HelloWorld
 * @USER: sxy
 * @DATE: 2022/4/25
 * @PROJECT_NAME: Sentinel-sxy
 */
public class HelloWorld {

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
        //初始化规则
        initFlowRule();
        //1、引入对应的依赖
        //2、定义资源
        while (true){
            Entry entry = null;
            try {
                //2.1 定义资源名称
                entry = SphU.entry("helloworld");
                //2.2 执行资源逻辑代码
                System.out.println("helloworld:datasource");
                System.out.println("helloworld:redis");
                Thread.sleep(20);

            }catch (BlockException e){
                System.out.println("要访问的资源被流控了，执行流控逻辑！");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (entry != null) {
                    entry.exit();
                }
            }
        }
        //3、定义规则
        //4、观察结果
    }
}
