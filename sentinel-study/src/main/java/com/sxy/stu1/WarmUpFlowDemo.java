package com.sxy.stu1;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @PACKAGE_NAME: com.sxy.stu1
 * @NAME: WarmUpFlowDemo
 * @USER: sxy
 * @DATE: 2022/5/3
 * @PROJECT_NAME: Sentinel-sxy
 */
public class WarmUpFlowDemo {

    private static final String KEY = "abc";

    private static volatile boolean stop = false;

    //定义全局统计信息
    private static AtomicInteger total = new AtomicInteger();
    private static AtomicInteger pass = new AtomicInteger();
    private static AtomicInteger block = new AtomicInteger();

    //定义运行时间
    private static int seconds = 60;

    public static void main(String[] args) throws InterruptedException {
        //初始化限流策略
        initFlowRule();
        //初始化全局限流
        Entry entry = null;
        try {
            entry = SphU.entry(KEY);
        } catch (BlockException e) {
        }finally {
            if (entry != null) {
                entry.exit();
            }
        }
        //启动检测线程
        Thread timer = new Thread(new TimerTask());
        timer.setName("sentinel-timer");
        timer.start();
        //初始时，系统运行以warm up
        for (int i = 0; i < 3; i++) {
            Thread t = new Thread(new WarmUpTask());
            t.setName("warm-up-task" + i);
            t.start();
        }
        Thread.sleep(2000);
        //开启更多的线程来运行，因为我们使用了WARMup，他会慢慢增加
        for (int i = 0; i < 32; i++) {
            Thread t = new Thread(new RunTask());
            t.setName("run-task"+i);
            t.start();
        }

    }

    private static void initFlowRule() {
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule();
        rule.setResource(KEY);
        rule.setCount(20);
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule.setLimitApp("default");
        //定义为WARM UP
        rule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_WARM_UP);
        //warm up过程中每秒通过数
        rule.setWarmUpPeriodSec(10);
        rules.add(rule);
        FlowRuleManager.loadRules(rules);
    }

    private static class TimerTask implements Runnable {
        @Override
        public void run() {
            long start = System.currentTimeMillis();
            System.out.println("begin to statistics");
            long oldTotal = 0;
            long oldPass = 0;
            long oldBloc = 0;
            while (!stop ){
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //统计信息
                long globalTotal = total.get();
                long oneSecondTotal = globalTotal - oldTotal;
                oldTotal = globalTotal;

                long golbalPass = pass.get();
                long oneSecondPass = golbalPass - oldPass;
                oldPass = golbalPass;

                long globalBlock = block.get();
                long oneSecondBlock = globalBlock - oldBloc;
                oldBloc = globalBlock;
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

    private static class WarmUpTask implements Runnable {
        @Override
        public void run() {
            //与业务线程一样
            while (!stop) {
                Entry entry = null;
                try {
                    entry = SphU.entry(KEY);
                    pass.addAndGet(1);
                } catch (BlockException e) {
                    block.incrementAndGet();
                }finally {
                    total.incrementAndGet();
                    if (entry != null) {
                        entry.exit();
                    }
                }
                Random random2 = new Random();
                try {
                    TimeUnit.MILLISECONDS.sleep(random2.nextInt(2000));
                } catch (InterruptedException e) {
                    // ignore
                }
            }
        }
    }

    private static class RunTask implements Runnable {

        @Override
        public void run() {
            while (!stop) {
                Entry entry = null;
                try {
                    entry = SphU.entry(KEY);
                    pass.addAndGet(1);
                } catch (BlockException e1) {
                    block.incrementAndGet();
                } catch (Exception e2) {
                    // biz exception
                } finally {
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
}
