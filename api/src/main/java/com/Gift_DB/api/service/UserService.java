package com.Gift_DB.api.service;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.Gift_DB.api.util.JwtUtil;
import com.Gift_DB.api.repository.UserRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    // Using constructor injection instead of @Autowired
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    // Login Method:
    // Responsible for calling methods to query the DB for a user with the provided credentials and validating
    // the credentials if a user is found. Returns a ResponseEntity to be used by the controller.
    public Map<String, Object> login(String credential, String password) {
        Map<String, Object> user = userRepository.getUserByCredential(credential);
        System.out.println(user);

        if (validatePassword(password, (String) user.get("hashedPassword"))) {
            // Return the 'Current User' scope as a hashmap. Note that the 'Current User'
            // includes all fields EXCEPT for the hashed password.
            return toCurrentUser(user);
        } else {
            // This else block will trigger if the user is found within the database, but
            // the login credentials are incorrect - UNAUTHORIZED
            // Or if the user is not found
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

    public Map<String, Object> restoreUser (String jwt) {
        int userId = jwtUtil.validateJwt(jwt);
        try {
            Map<String, Object> user = userRepository.getUserById(userId);
            return toCurrentUser(user);
        } catch (EmptyResultDataAccessException e) {
            return new HashMap<>();
        }
    }

    // toCurrentUser generates and returns a hashmap of all user data EXCEPT the hashed password
    private Map<String, Object> toCurrentUser(Map<String, Object> user) {
        Map<String, Object> currentUser = new HashMap<>();
        currentUser.put("id", user.get("id"));
        currentUser.put("userName", user.get("userName"));
        currentUser.put("email", user.get("email"));
        currentUser.put("accountAdmin", user.get("accountAdmin"));
        currentUser.put("linkedTo", user.get("linkedTo"));
        currentUser.put("active", user.get("active"));
        currentUser.put("name", user.get("name"));
        currentUser.put("createdAt", user.get("createdAt"));
        currentUser.put("updatedAt", user.get("updatedAt"));

        return currentUser;
    }

    // toSafeObject generates and returns a hashmap of limited user data. Typical use - JWT creation
    public Map<String, Object> toSafeObject(Map<String, Object> currentUser) {
        Map<String, Object> safeObject = new HashMap<>();
        safeObject.put("id", currentUser.get("id"));
        safeObject.put("userName", currentUser.get("userName"));
        safeObject.put("email", currentUser.get("email"));

        return safeObject;
    }

}
