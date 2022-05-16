package com.sxy.annotation.service;

import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.sxy.annotation.util.ExceptionUtil;
import org.springframework.stereotype.Service;

/**
 * @PACKAGE_NAME: com.sxy.annotation.service
 * @NAME: AnnoServiceImpl
 * @USER: sxy
 * @DATE: 2022/5/15
 * @PROJECT_NAME: Sentinel-sxy
 */
@Service
public class AnnoServiceImpl implements AnnoService{

    private static int count = 0;

    @Override
    @SentinelResource(
            value = "com.sxy.annotation.service.AnnoService:block",
            entryType = EntryType.OUT,
            blockHandler = "blockHandler"
    )
    public String block() {
        return "成功";
    }

    public String blockHandler(BlockException e){
        System.out.println("进入流控策略--》block");
        return "block hander";
    }

    @Override
    @SentinelResource(
            value = "com.sxy.annotation.service.AnnoService:fallback",
            entryType = EntryType.OUT,
            blockHandler = "fallbackBlockHandler",
            blockHandlerClass = {ExceptionUtil.class},
            fallback = "fallbackHandler"
    )
    public String fallback() {
        count++;
        if (count % 4 == 0) {
            throw new RuntimeException("抛出业务异常");
        }
        return "成功";
    }

    public String fallbackHandler(Throwable throwable) {
        System.out.println("fallback exception --> error");
        return "fallback";
    }
}
