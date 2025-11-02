package ApexCollectiveHibernateBE.ApexCollective.Model;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginModel {
    private String email;
    private String password;
    private Boolean isDirectLogin;
}
