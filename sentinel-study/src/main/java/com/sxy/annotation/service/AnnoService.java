package com.sxy.annotation.service;

public interface AnnoService {

    /**
     * 测试block限流的服务
     * @return
     */
    String block();

    /**
     * 测试fallback服务
     * @return
     */
    String fallback();

}
