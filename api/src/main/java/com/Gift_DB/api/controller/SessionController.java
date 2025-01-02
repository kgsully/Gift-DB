package com.Gift_DB.api.controller;

import com.Gift_DB.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class SessionController {

    @Autowired
    private UserService userService;

    // Login Route:
    // Calls the login method from the user service (which returns a ResponseEntity which may contain a
    // hashmap with user information, or an error. Sets the token cookie for the session, and returns the user info
    // in the event of a successful login.
    @PostMapping("/session")
    public ResponseEntity<?> login(@RequestBody Map<String, String> req) {
        String credential = req.get("credential");
        String password = req.get("password");

        try {
            // TODO Call method to set token cookie to send to client upon successful authentication
            Map<String, Object> loginResponse = userService.login(credential, password);
            if (loginResponse.containsKey("id")) {
                return new ResponseEntity<>(loginResponse, HttpStatus.OK);
            } else {
                // This else block will trigger if the user is found within the database, but
                // the login credentials are incorrect - UNAUTHORIZED
                return new ResponseEntity<>(loginResponse, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            // This catch block will trigger if the user is not found within the database - BAD REQUEST
            return new ResponseEntity<>(userService.generateLoginError(credential, password), HttpStatus.BAD_REQUEST);

        }
    }
}
