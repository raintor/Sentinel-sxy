package com.sxy.indashboard;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @PACKAGE_NAME: com.sxy.indashboard
 * @NAME: FlowController
 * @USER: sxy
 * @DATE: 2022/5/12
 * @PROJECT_NAME: Sentinel-sxy
 */
@RestController
public class FlowController {
    @RequestMapping("/flow")
    public String getFlow(){
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
        return "flow";
    }
}
