package akletini.life.core.user.repository.entity;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Embeddable
public class TokenContainer {

    private String accessToken;

    private String accessTokenCreation;
    @Size(min = 10)
    private String refreshToken;
}
