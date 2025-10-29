package ApexCollectiveHibernateBE.ApexCollective.Model;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class TokenModel {
    private String accessToken;
    private String refreshToken;
    private String user;
    private Integer expiresIn;
    private Integer refreshExpiresIn;
}
