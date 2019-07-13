package com.pinyougou.shop.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RequestMapping("/login")
@RestController
public class LoginController {

    @GetMapping("/getUsername")
    public Map<String, Object> getUsername() {
        Map<String, Object> map = new HashMap<>();
        //获取当前登录的用户名
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        map.put("username", username);
        return map;
    }
}
