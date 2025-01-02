package com.Gift_DB.api.service;

import com.Gift_DB.api.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.Gift_DB.api.repository.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // TODO - Look at refactoring the login method so as not to return a response entity
    // Login Method:
    // Responsible for calling methods to query the DB for a user with the provided credentials and validating
    // the credentials if a user is found. Returns a ResponseEntity to be used by the controller.
    public Map<String, Object> login(String credential, String password) {

        User user = userRepository.getUserLogin(credential);
        if (validatePassword(password, user.getHashedPassword())) {
            // Return the 'Current User' scope as a hashmap. Note that the 'Current User'
            // includes all fields EXCEPT for the hashed password.
            return toCurrentUser(user);
        } else {
            // This else block will trigger if the user is found within the database, but
            // the login credentials are incorrect - UNAUTHORIZED
            return generateLoginError(credential, password);
        }

    }

    // Method to user the Bcrypt library to validate the user provided password matches
    // what is found for the user in the database.
    private boolean validatePassword(String password, String hashedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(password, hashedPassword);
    }

    // Generate login error to pass to the response based upon the problem found.
    // If the user provides a blank / empty username/email or password, respond as such,
    // otherwise the failure is due to either the user is not found in the DB (SQLException) or
    // the password doesn't match the DB record - if this is the case, provide a generic invalid credentials message.
    public Map<String, Object> generateLoginError(String credential, String password) {
        Map<String, Object> msg = new HashMap<String, Object>();
        List<String> errors = new ArrayList<String>();

        msg.put("title", "Login Failed");
        if (credential.isBlank() || password.isBlank()) {
            if (credential.isBlank()) {
                errors.add("Please provide a valid username or email address");
            }
            if (password.isBlank()) {
                errors.add("Please provide a password");
            }
        } else {
            errors.add("The provided login credentials were invalid.");
        }

        msg.put("errors", errors);
        return msg;
    }

    // Create and return a hashmap from the passed in user to be used in the ResponseEntity for a successful login.
    // This prevents the hashed password from being sent back and being present on the frontend.
    private Map<String, Object> toCurrentUser(User user) {
        Map<String, Object> currentUser = new HashMap<>();
        currentUser.put("id", user.getId());
        currentUser.put("username", user.getUsername());
        currentUser.put("email", user.getEmail());
        currentUser.put("createdAt", user.getCreatedAt());
        currentUser.put("updatedAt", user.getUpdatedAt());

        return currentUser;
    }
}
