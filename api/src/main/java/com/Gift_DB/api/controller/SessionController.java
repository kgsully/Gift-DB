package com.Gift_DB.api.controller;

import com.Gift_DB.api.service.UserService;
import com.Gift_DB.api.util.JwtUtil;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.env.Environment;

import java.util.Map;

@RestController
public class SessionController {

    // Using constructor injection instead of @Autowired
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final Environment env;

    public SessionController(UserService userService, JwtUtil jwtUtil, Environment env) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.env = env;
    }

    // @Autowired
    // private UserService userService;
    // private Environment env;

    // Login Route:
    // Calls the login method from the user service (which returns a ResponseEntity which may contain a
    // hashmap with user information, or an error). Sets the token cookie for the session, and returns the user info
    // in the event of a successful login.
    @PostMapping("/session")
    public ResponseEntity<?> login(@RequestBody Map<String, String> req) {
        String credential = req.get("credential");
        String password = req.get("password");

        try {
            // TODO Update jwt cookie configuration:
            // TODO    - secure and sameSite based upon environment (dev / prod)
            // TODO    - maxAge to be pulled from environment variable for JWT expiration time
            Map<String, Object> loginResponse = userService.login(credential, password);
            if (loginResponse.containsKey("id")) {

                String jwt = jwtUtil.generateJwt(userService.toSafeObject(loginResponse));
                ResponseCookie jwtCookie = ResponseCookie.from("Session_Token", jwt)
                        .httpOnly(true)
                        //.secure(isProduction)
                        .maxAge(3600) // Note that this is set for 1 hr as maxAge expects ms. Can update this to pull from the env variable later
                        //.sameSite(isProduction && "Lax")
                        .build();
                // Previous ResponseEntity return
                // return new ResponseEntity<>(loginResponse, HttpStatus.OK);

                // Attach cookie to response entity header upon successful authorization
                return ResponseEntity.ok()
                        .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                        .body(loginResponse);
            } else {
                // This else block will trigger if the user is found within the database, but
                // the login credentials are incorrect - UNAUTHORIZED
                // Remove any JWT cookie present
                ResponseCookie deleteJwtCookie = ResponseCookie.from("Session_Token", "")
                        .maxAge(0)
                        .build();

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .header(HttpHeaders.SET_COOKIE, deleteJwtCookie.toString())
                        .body(loginResponse);

                // Old response entity return code
                // return new ResponseEntity<>(loginResponse, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            // This catch block will trigger if the user is not found within the database - BAD REQUEST
            // Remove any JWT cookie present
            ResponseCookie deleteJwtCookie = ResponseCookie.from("Session_Token", "")
                    .maxAge(0)
                    .build();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .header(HttpHeaders.SET_COOKIE, deleteJwtCookie.toString())
                    .body(userService.generateLoginError(credential, password));

            // Old response entity return code
            // return new ResponseEntity<>(userService.generateLoginError(credential, password), HttpStatus.BAD_REQUEST);

        }
    }
}
