package ApexCollectiveHibernateBE.ApexCollective.ServiceImp;

import ApexCollectiveHibernateBE.ApexCollective.Common.ApexCollectiveException;
import ApexCollectiveHibernateBE.ApexCollective.Entity.User;
import ApexCollectiveHibernateBE.ApexCollective.Entity.UserSessions;
import ApexCollectiveHibernateBE.ApexCollective.JwtConfiguration.JwtUtility;
import ApexCollectiveHibernateBE.ApexCollective.Model.LoginModel;
import ApexCollectiveHibernateBE.ApexCollective.Model.TokenModel;
import ApexCollectiveHibernateBE.ApexCollective.Repositories.UserRegisterRepository;
import ApexCollectiveHibernateBE.ApexCollective.Repositories.UserSessionRepository;
import ApexCollectiveHibernateBE.ApexCollective.Services.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ApexCollectiveHibernateBE.ApexCollective.Common.ExceptionMessage;

import java.util.Objects;
import java.util.Optional;

@Service
public class LoginServiceImp implements LoginService {

    private static final Logger log = LoggerFactory.getLogger(LoginServiceImp.class);
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRegisterRepository userRegisterRepository;

    @Autowired
    private JwtUtility jwtUtility;

    @Autowired
    private UserSessionRepository userSessionRepository;

    private static final int MAX_LOGIN_ATTEMPTS = 5;

    @Override
    public TokenModel loginUser(LoginModel loginModel) throws ApexCollectiveException {
        log.info("Starting login process for email: {}", loginModel.getEmail());

        UserSessions userSessions = new UserSessions();

        if (loginModel.getEmail() == null || loginModel.getEmail().trim().isEmpty()) {
            log.warn("Login attempt failed: Email is required");
            throw new ApexCollectiveException(ExceptionMessage.EMAIL_IS_REQUIRED);
        }

        if (loginModel.getPassword() == null || loginModel.getPassword().trim().isEmpty()) {
            log.warn("Login attempt failed: Password is required for email: {}", loginModel.getEmail());
            throw new ApexCollectiveException(ExceptionMessage.PASSWORD_IS_REQUIRED);
        }

        Optional<User> userOptional = userRegisterRepository.findByEmail(loginModel.getEmail());

        if (userOptional.isEmpty()) {
            log.warn("Login attempt failed: User not found with email: {}", loginModel.getEmail());
            throw new ApexCollectiveException(ExceptionMessage.INVALID_EMAIL_OR_PASSWORD);
        }



        User user = userOptional.get();

        Optional<UserSessions> existingSessionOpt = userSessionRepository.findUserByUserId(user.getId());
        int currentLoginAttempts = 0;

        if (existingSessionOpt.isPresent()) {
            UserSessions existingSession = existingSessionOpt.get();
            currentLoginAttempts = existingSession.getLogin_attempt();
            if (currentLoginAttempts >= MAX_LOGIN_ATTEMPTS) {
                log.warn("Login blocked: Too many attempts for user: {}", user.getEmail());
                throw new ApexCollectiveException(ExceptionMessage.TOO_MANY_ATTEMPTS);
            }
        }

        if (!passwordEncoder.matches(loginModel.getPassword(), user.getPassword())) {
            log.warn("Login attempt failed: Invalid password for email: {}", loginModel.getEmail());

            int newLoginAttempts = currentLoginAttempts + 1;

            UserSessions userSession = existingSessionOpt.orElse(new UserSessions());
            userSession.setUser_id(user.getId());
            userSession.setLogin_attempt(newLoginAttempts);
            userSessionRepository.save(userSession);

            if (newLoginAttempts >= MAX_LOGIN_ATTEMPTS) {
                throw new ApexCollectiveException(ExceptionMessage.TOO_MANY_ATTEMPTS);
            }

            throw new ApexCollectiveException(ExceptionMessage.INVALID_EMAIL_OR_PASSWORD);
        }

        String token = jwtUtility.generateTokenFromUsername(user.getEmail());
        String refreshToken = jwtUtility.generateRefreshTokenFromUser(user);
        int expiresIn =  jwtUtility.getJwtExpirationMs() / 1000;
        int refreshTokenExpiresIn = jwtUtility.getRefreshExpirationMs() / 1000;

        TokenModel tokenModel = new TokenModel();
        tokenModel.setAccessToken(token);
        tokenModel.setRefreshToken(refreshToken);
        tokenModel.setExpiresIn(expiresIn);
        tokenModel.setRefreshExpiresIn(refreshTokenExpiresIn);

        log.info("Login successful for email: {}, user ID: {}", loginModel.getEmail(), user.getId());


        userSessions.setUser_id(user.getId());
        userSessions.setAccess_token(token);
        userSessions.setAccessTokenExpiredAt(expiresIn);
        userSessions.setRefresh_token(refreshToken);
        userSessions.setRefreshTokenExpiredAt(refreshTokenExpiresIn);
        userSessions.setLogin_attempt(0);

        userSessionRepository.save(userSessions);

        return tokenModel;
    }
}