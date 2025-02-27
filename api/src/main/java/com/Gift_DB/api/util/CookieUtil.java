package com.Gift_DB.api.util;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    public ResponseCookie generateSessionCookie(String jwt) {

        // TODO Update jwt cookie configuration:
        // TODO    - secure and sameSite based upon environment (dev / prod)
        // TODO    - maxAge to be pulled from environment variable for JWT expiration time
        return ResponseCookie.from("Session_Token", jwt)
                .httpOnly(true)
                //.secure(isProduction)
                .maxAge(3600) // Note that this is set for 1 hr as maxAge expects ms.
                //.sameSite(isProduction && "Lax")
                .build();
    }

    public ResponseCookie deleteSessionCookie() {
        return ResponseCookie.from("Session_Token", "")
                .maxAge(0)
                .build();
    }

}
