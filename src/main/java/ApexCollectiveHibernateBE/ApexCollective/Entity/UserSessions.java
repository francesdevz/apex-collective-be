package ApexCollectiveHibernateBE.ApexCollective.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "user_session")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserSessions implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long user_id;

    @Column(name = "access_token", columnDefinition = "TEXT")
    private String access_token;

    @Column(name = "refresh_token", columnDefinition = "TEXT")
    private String refresh_token;

    @Column(name = "accessTokenExpiredAt")
    private int accessTokenExpiredAt;

    @Column(name = "refreshTokenExpiredAt")
    private int refreshTokenExpiredAt;

    @Column(name = "login_attempt")
    private int login_attempt;



}
