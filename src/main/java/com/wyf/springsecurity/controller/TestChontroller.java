package com.wyf.springsecurity.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangyifan
 */
@RestController
@RequestMapping("/test")
public class TestChontroller {

    @GetMapping("/hello")
    public String hello(){
        return "hello security~~~";
    }

    @GetMapping("/index")
    public String index(){
        return "hello index~~~";
    }

    @GetMapping("/update")
    @Secured({"ROLE_manager"})
    public String update(){
        return "hello manager~~~";
    }

    @GetMapping("/preAuth")
    @PreAuthorize("hasAnyAuthority('amind','program')")
    public String preAuth(){
        return "hello admin~~~";
    }

    @GetMapping("/postAuth")
    @PostAuthorize("hasAnyAuthority('admin','program')")
    public String postAuth(){
        System.out.println("虽然没权限，但我执行了！！！！");
        return "hello admin~~~";
    }
}
