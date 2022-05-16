package com.sxy.annotation;

import com.sxy.annotation.service.AnnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @PACKAGE_NAME: com.sxy.annotation
 * @NAME: AnnoController
 * @USER: sxy
 * @DATE: 2022/5/15
 * @PROJECT_NAME: Sentinel-sxy
 */
@RestController
public class AnnoController {

    @Autowired
    private AnnoService annoService;

    @RequestMapping("/block")
    public String testBlock(){
        return annoService.block();
    }

    @RequestMapping("/fallback")
    public String testFallBack(){
        return annoService.fallback();
    }
}
