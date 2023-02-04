package akletini.life.user.repository.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class TokenContainer {

    private String accessToken;

    private String accessTokenCreation;
    private String refreshToken;
}
