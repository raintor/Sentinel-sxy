package com.sxy.annotation.service;

import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
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
    public String fallback() {
        return null;
    }
}
