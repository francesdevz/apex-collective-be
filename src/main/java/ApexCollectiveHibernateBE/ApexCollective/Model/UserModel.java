package ApexCollectiveHibernateBE.ApexCollective.Model;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class UserModel {

    private String fullName;
    private String email;
    private String password;
    private Boolean directLogin;

}
