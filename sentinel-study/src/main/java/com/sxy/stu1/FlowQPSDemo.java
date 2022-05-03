package com.sxy.stu1;

/**
 * @PACKAGE_NAME: com.sxy.stu1
 * @NAME: FlowQPSDemo
 * @USER: sxy
 * @DATE: 2022/5/3
 * @PROJECT_NAME: Sentinel-sxy
 */

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.Rule;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 1、限流的直接表现就是抛出FlowException，它是BlockException的子类
 */
public class FlowQPSDemo {

    //定义资源
    private static final String KEY = "abc";
    //定义结束表示
    private static volatile boolean stop = false;
    //定义统计信息
    //总过通过数目
    private static AtomicInteger pass = new AtomicInteger();
    //总数
    private static AtomicInteger total = new AtomicInteger();
    //总共阻塞数
    private static AtomicInteger block = new AtomicInteger();

    private static final int threadCount = 32;

    private static int seconds = 60;


    public static void main(String[] args) {
        //初始化流控规则
        initFoleRule();
        //启动统计线程
        tick();
        //启动业务线程
        simulatedTraffic();
        System.out.println("===== begin to do flow control");
        System.out.println("only 20 requests per second can pass");
    }

    private static void simulatedTraffic() {
        //启动业务线程
        for (int i = 0; i < threadCount; i++) {
            Thread t = new Thread(new RunTask());
            t.setName("simulate-traffic-task" + i);
            t.start();
        }

    }

    private static void tick() {
        Thread timer = new Thread(new TimerTask());
        timer.setName("sentinel-timer-task");
        timer.start();
    }

    static class RunTask implements Runnable{
        @Override
        public void run() {
            while (!stop) {
                Entry entry = null;
                try {
                    entry = SphU.entry(KEY);
                    //统计通过的
                    pass.addAndGet(1);
                } catch (BlockException e) {
                    //被阻塞
                    block.incrementAndGet();
                } catch (Exception e) {
                    //业务异常
                }finally {
                    //统计总数
                    total.incrementAndGet();
                    if (entry != null) {
                        entry.exit();
                    }
                }
                Random random2 = new Random();
                try {
                    TimeUnit.MILLISECONDS.sleep(random2.nextInt(50));
                } catch (InterruptedException e) {
                    // ignore
                }
            }
        }
    }

    static class TimerTask implements Runnable{
        @Override
        public void run() {
            long start = System.currentTimeMillis();
            System.out.println("begin to statistics");
            long oldTotal = 0;
            long oldPass = 0;
            long oldBlock = 0;
            while (!stop){
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {

                }
                //统计总数
                long globalTotal = total.get();
                long oneSecondTotal = globalTotal - oldTotal;
                oldTotal = globalTotal;

                //统计通过数
                long globalPass = pass.get();
                long oneSecondPass = globalPass - oldPass;
                oldPass = globalPass;

                //统计block数
                long globalBlock = block.get();
                long oneSecondBlock = globalBlock - oldBlock;
                oldBlock = globalBlock;

                System.out.println(seconds + " send qps is: " + oneSecondTotal);
                System.out.println(TimeUtil.currentTimeMillis() + ", total:" + oneSecondTotal
                        + ", pass:" + oneSecondPass
                        + ", block:" + oneSecondBlock);

                if (seconds-- < 0) {
                    stop = true;
                }
            }
            long cost = System.currentTimeMillis() - start;
            System.out.println("time cost: " + cost + " ms");
            System.out.println("total:" + total.get() + ", pass:" + pass.get()
                    + ", block:" + block.get());
            System.exit(0);
        }
    }

    private static void initFoleRule() {
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule();
        rule.setResource(KEY);
        //QPS限流20
        rule.setCount(20);
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        //default不区分调用来源
        rule.setLimitApp("default");
        rules.add(rule);
        FlowRuleManager.loadRules(rules);
    }


}
