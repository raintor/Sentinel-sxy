package com.sxy.annotation;

import com.sxy.indashboard.InDashBoardApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @PACKAGE_NAME: com.sxy.annotation
 * @NAME: AnnoApplication
 * @USER: sxy
 * @DATE: 2022/5/15
 * @PROJECT_NAME: Sentinel-sxy
 */
@SpringBootApplication
public class AnnoApplication {
    public static void main(String[] args) {
        SpringApplication.run(AnnoApplication.class, args);
        System.out.println("加载配置完毕");
    }
}
