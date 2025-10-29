package ApexCollectiveHibernateBE.ApexCollective.ServiceImp;


import ApexCollectiveHibernateBE.ApexCollective.Common.ApexCollectiveException;
import ApexCollectiveHibernateBE.ApexCollective.Entity.User;
import ApexCollectiveHibernateBE.ApexCollective.JwtConfiguration.JwtUtility;
import ApexCollectiveHibernateBE.ApexCollective.Model.LoginModel;
import ApexCollectiveHibernateBE.ApexCollective.Model.TokenModel;
import ApexCollectiveHibernateBE.ApexCollective.Repositories.UserRegisterRepository;
import ApexCollectiveHibernateBE.ApexCollective.Services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginServiceImp implements LoginService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRegisterRepository userRegisterRepository;

    @Autowired
    private JwtUtility jwtUtility;

    @Override
    public TokenModel loginUser(LoginModel loginModel) throws ApexCollectiveException {

        if (loginModel.getEmail() == null || loginModel.getEmail().trim().isEmpty()) {
            throw new ApexCollectiveException("Email is required");
        }

        if (loginModel.getPassword() == null || loginModel.getPassword().trim().isEmpty()) {
            throw new ApexCollectiveException("Password is required");
        }

        Optional<User> userOptional = userRegisterRepository.findByEmail(loginModel.getEmail());

        if (userOptional.isEmpty()) {
            throw new ApexCollectiveException("Invalid email or password");
        }


        User user = userOptional.get();

        if (!passwordEncoder.matches(loginModel.getPassword(), user.getPassword())) {
            throw new ApexCollectiveException("Invalid email or password");
        }

        String token = jwtUtility.generateTokenFromUsername(user.getEmail());
        String refreshToken = jwtUtility.generateRefreshTokenFromUser(user);

        TokenModel tokenModel = new TokenModel();
        tokenModel.setAccessToken(token);
        tokenModel.setRefreshToken(refreshToken);
        tokenModel.setExpiresIn(jwtUtility.getJwtExpirationMs() / 1000);
        tokenModel.setRefreshExpiresIn(jwtUtility.getRefreshExpirationMs() / 1000);

        return tokenModel;
    }


}
