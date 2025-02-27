package com.Gift_DB.api.controller;

import java.util.HashMap;
import java.util.Map;

import com.Gift_DB.api.service.UserService;
import com.Gift_DB.api.util.JwtUtil;
import com.Gift_DB.api.util.CookieUtil;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

@RestController
public class SessionController {

    // Using constructor injection instead of @Autowired
    private final Environment env;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;

    public SessionController(Environment env, UserService userService, JwtUtil jwtUtil, CookieUtil cookieUtil) {
        this.env = env;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.cookieUtil = cookieUtil;
    }

    // Login Route:
    // Calls the login method from the user service (which returns a ResponseEntity which may contain a
    // hashmap with user information, or an error). Sets the token cookie for the session, and returns the user info
    // in the event of a successful login.
    @PostMapping("/session")
    public ResponseEntity<?> login(@RequestBody Map<String, String> req) {
        String credential = req.get("credential");
        String password = req.get("password");

        try {

            Map<String, Object> loginResponse = userService.login(credential, password);
            if (loginResponse.containsKey("id")) {

                String jwt = jwtUtil.generateJwt(userService.toSafeObject(loginResponse));
                ResponseCookie sessionCookie = cookieUtil.generateSessionCookie(jwt);

                // Attach cookie to response entity header upon successful authorization
                return ResponseEntity.ok()
                        .header(HttpHeaders.SET_COOKIE, sessionCookie.toString())
                        .body(loginResponse);
            } else {
                // This else block will trigger if the user is found within the database, but
                // the login credentials are incorrect - UNAUTHORIZED
                // Remove any JWT cookie present
                ResponseCookie deleteSessionCookie = cookieUtil.deleteSessionCookie();

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .header(HttpHeaders.SET_COOKIE, deleteSessionCookie.toString())
                        .body(loginResponse);
            }
        } catch (Exception e) {
            // This catch block will trigger if the user is not found within the database - BAD REQUEST
            // Remove any JWT cookie present
            ResponseCookie deleteSessionCookie = cookieUtil.deleteSessionCookie();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .header(HttpHeaders.SET_COOKIE, deleteSessionCookie.toString())
                    .body(userService.generateLoginError(credential, password));
        }
    }

    // Logout Route:
    // The DELETE /api/session logout route will remove the session token cookie from the response (ending session and removing auth)
    // and return a JSON success message
    @DeleteMapping("/session")
    public ResponseEntity<?> logout() {

        Map<String, String> msg = new HashMap<>();
        msg.put("message", "Success");
        ResponseCookie deleteSessionCookie = cookieUtil.deleteSessionCookie();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteSessionCookie.toString())
                .body(msg);
    }
    
}
