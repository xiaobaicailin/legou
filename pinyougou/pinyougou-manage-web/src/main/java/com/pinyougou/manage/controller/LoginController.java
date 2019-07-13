package com.pinyougou.manage.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/login")
@RestController
public class LoginController {

    @GetMapping("/getUsername.do")
    public Map<String,String> getUsername(){
        Map<String,String> map = new HashMap<>();
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        map.put("username",name );
        return map;
    }
}
