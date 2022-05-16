package com.sxy.annotation.util;

import com.alibaba.csp.sentinel.slots.block.BlockException;

/**
 * @PACKAGE_NAME: com.sxy.annotation.util
 * @NAME: ExceptionUtil
 * @USER: sxy
 * @DATE: 2022/5/16
 * @PROJECT_NAME: Sentinel-sxy
 */
public final class ExceptionUtil {
    public static String fallbackBlockHandler(BlockException e){
        System.out.println("block --->");
        return "block";
    }
}
