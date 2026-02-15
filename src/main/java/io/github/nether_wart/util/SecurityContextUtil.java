package io.github.nether_wart.util;

import io.github.nether_wart.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContextUtil {
    public static Long getUid() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object object = authentication.getDetails();
            if (object instanceof User user) {
                return user.getUid();
            }
        }
        return null;
    }
}
