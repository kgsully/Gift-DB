package com.Gift_DB.api.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.env.Environment;

@RestController
public class BaseController {

    @Autowired
    private Environment env;

    @GetMapping("/")
    public String home() {
        return "Hello World!" + env.getProperty("jwt.secret");
    }
}
