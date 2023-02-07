package akletini.life.user.structure;

import akletini.life.user.repository.entity.AuthProvider;
import akletini.life.user.repository.entity.TokenContainer;
import akletini.life.user.repository.entity.User;

import java.util.Date;

public class TestUsers {

    public static User getDefaultCredentialUser() {
        User user = new User();
        user.setName("Horst");
        user.setPassword("Top-Secret");
        user.setLoggedIn(true);
        user.setEmail("horst@horst.de");
        user.setAuthProvider(AuthProvider.CREDENTIALS);
        user.setTokenContainer(getTokenContainer());
        return user;
    }

    public static User getDefaultGoogleAuthUser() {
        User user = new User();
        user.setName("Horst");
        user.setLoggedIn(true);
        user.setEmail("horst@gmail.de");
        user.setAuthProvider(AuthProvider.GOOGLE);
        user.setTokenContainer(getTokenContainer());
        return user;
    }

    public static TokenContainer getTokenContainer() {
        TokenContainer tokenContainer = new TokenContainer();
        tokenContainer.setAccessToken("Aaaa");
        tokenContainer.setAccessTokenCreation(new Date().toString());
        tokenContainer.setRefreshToken("Aaaa");
        return tokenContainer;
    }

}
