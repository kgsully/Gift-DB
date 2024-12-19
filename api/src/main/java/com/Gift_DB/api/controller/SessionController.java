package com.Gift_DB.api.controller;

import com.Gift_DB.api.dto.User;
import com.Gift_DB.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SessionController {

    @Autowired
    private UserService userService;

    // Login Route:
    // Calls the login method from the user service (which returns a ResponseEntity which may contain a
    // hashmap with user information, or an error. Sets the token cookie for the session, and returns the user info
    // in the event of a successful login.
    // TODO - Look at refactoring such that the ResponseEntity is generated here instead of the service
    @PostMapping("/session")
    public ResponseEntity<?> login(@RequestBody Map<String, String> req) {
        String credential = req.get("credential");
        String password = req.get("password");

        // TODO Call method to set token cookie to send to client upon successful authentication

        return userService.login(credential, password);
    }

}
