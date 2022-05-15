package com.sxy.annotation;

import com.alibaba.csp.sentinel.annotation.aspectj.SentinelResourceAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @PACKAGE_NAME: com.sxy.annotation
 * @NAME: AnnoConfiguration
 * @USER: sxy
 * @DATE: 2022/5/15
 * @PROJECT_NAME: Sentinel-sxy
 */
@Configuration
public class AnnoConfiguration {
    @Bean
    public SentinelResourceAspect sentinelResourceAspect(){
        return new SentinelResourceAspect();
    }
}
