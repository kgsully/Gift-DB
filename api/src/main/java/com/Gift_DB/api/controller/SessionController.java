package com.Gift_DB.api.controller;

import java.util.HashMap;
import java.util.Map;

import com.Gift_DB.api.service.UserService;
import com.Gift_DB.api.util.JwtUtil;
import com.Gift_DB.api.util.CookieUtil;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SessionController {

    // Using constructor injection instead of @Autowired
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;

    public SessionController(UserService userService, JwtUtil jwtUtil, CookieUtil cookieUtil) {
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


        Map<String, Object> loginResponse = userService.login(credential, password);
        if (loginResponse.containsKey("id")) {
            String jwt = jwtUtil.generateJwt(userService.toSafeObject(loginResponse));

            Map<String, Object> user = new HashMap<>();
            user.put("user", loginResponse);

            // Attach cookie to response entity header upon successful authorization
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookieUtil.generateSessionCookie(jwt).toString())
                    .body(user);
        } else {
            return switch ((int) loginResponse.get("status")) {
                case 400 -> ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .header(HttpHeaders.SET_COOKIE, cookieUtil.deleteSessionCookie().toString())
                        .body(userService.generateLoginError(credential, password, 400));
                case 401 -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .header(HttpHeaders.SET_COOKIE, cookieUtil.deleteSessionCookie().toString())
                        .body(loginResponse);
                default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .header(HttpHeaders.SET_COOKIE, cookieUtil.deleteSessionCookie().toString())
                        .body("Internal Server Error");
            };
        }
    }

    // Logout Route:
    // The DELETE /api/session logout route will remove the session token cookie with the response
    // and return a JSON success message
    @DeleteMapping("/session")
    public ResponseEntity<?> logout() {
        Map<String, String> msg = new HashMap<>();
        msg.put("message", "Success");

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookieUtil.deleteSessionCookie().toString())
                .body(msg);
    }

    // Get Session User Route:
    // Uses UserService restoreUser and will return the session user as JSON
    // If there is not a valid session (e.g. no JWT session cookie or expired JWT),
    // it will clear session token and return a JSON with an empty object under the user key.
    @GetMapping("/session")
    public ResponseEntity<?> getUser(@CookieValue(value = "Session_Token", defaultValue = "none") String sessionToken) {
        Map<String, Object> restoredUser = userService.restoreUser(sessionToken);
        Map<String, Object> user = new HashMap<>();
        user.put("user", restoredUser);

        if (restoredUser.containsKey("id")) {
            return ResponseEntity.ok()
                    .body(user);
        } else {
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookieUtil.deleteSessionCookie().toString())
                    .body(user);
        }
    }

}
