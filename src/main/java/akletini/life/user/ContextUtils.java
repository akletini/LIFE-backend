package akletini.life.user;

import akletini.life.user.repository.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@AllArgsConstructor
public class ContextUtils {

    public static User getCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        User currentUser = null;
        if (authentication != null) {
            currentUser = (User) authentication.getPrincipal();
        }
        if (currentUser != null) {
            return currentUser;
        }
        SecurityContextHolder.clearContext();
        throw new RuntimeException("No user found in context");
    }
}
