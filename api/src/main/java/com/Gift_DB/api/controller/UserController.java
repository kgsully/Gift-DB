package com.Gift_DB.api.controller;

import com.Gift_DB.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user/{userName}")
    public List<Map<String, Object>> returnUser(@PathVariable String userName) {
        return userService.getUserByUserName(userName);
    }

}
