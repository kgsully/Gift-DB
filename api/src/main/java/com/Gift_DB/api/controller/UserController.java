package com.Gift_DB.api.controller;

import com.Gift_DB.api.model.User;
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

    // Route for querying the database for a user of specific username. In the event that
    // the query does not find the specified user, an 'empty' user with default values will be returned.
    @GetMapping("/user/{userName}")
    public User returnUser(@PathVariable String userName) {
        return userService.getUserByUserName(userName);
    }

}
