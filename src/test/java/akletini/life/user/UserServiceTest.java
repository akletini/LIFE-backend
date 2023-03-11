package akletini.life.user;

import akletini.life.user.repository.entity.User;
import akletini.life.user.service.api.UserService;
import akletini.life.user.structure.TestUsers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    public void saveEmptyPasswordOnCredentials() {
        // Given
        User credentialUser = TestUsers.getDefaultCredentialUser();
        User googleAuthUser = TestUsers.getDefaultGoogleAuthUser();

        // When
        credentialUser.setPassword(null);

        // Then
        assertThrows(Exception.class, () -> userService.store(credentialUser));
        assertDoesNotThrow(() -> userService.store(googleAuthUser));
    }
}
