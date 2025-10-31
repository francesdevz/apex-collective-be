package ApexCollectiveHibernateBE.ApexCollective.ServiceImp;

import ApexCollectiveHibernateBE.ApexCollective.Common.ApexCollectiveException;
import ApexCollectiveHibernateBE.ApexCollective.Common.ApiResponse;
import ApexCollectiveHibernateBE.ApexCollective.Entity.User;
import ApexCollectiveHibernateBE.ApexCollective.Model.UserModel;
import ApexCollectiveHibernateBE.ApexCollective.Repositories.UserRegisterRepository;
import ApexCollectiveHibernateBE.ApexCollective.Services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ApexCollectiveHibernateBE.ApexCollective.Common.ExceptionMessage;

import java.util.Optional;

@Service
@Slf4j
public class UserServiceImp implements UserService {

    @Autowired
    private UserRegisterRepository userRegisterRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<ApiResponse> registerUser(UserModel userModel) throws ApexCollectiveException {
        log.info("Starting user registration process for email: {}", userModel.getEmail());

        try {

            log.debug("Checking if user already exists with email: {}", userModel.getEmail());

            Optional<User> isUserAlreadyExist = userRegisterRepository.findByEmail(userModel.getEmail());

            if(!isUserAlreadyExist.isEmpty()) {
                log.warn("Registration failed: Email already exists - {}", userModel.getEmail());
                throw new ApexCollectiveException(ExceptionMessage.EMAIL_ALREADY_EXISTED);
            }

            if(!userModel.getDirectLogin()) {
                log.debug("Encoding password for new user");
                userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
            }

            log.debug("Creating new user entity");
            User user = new User();
            user.setFullName(userModel.getFullName());
            user.setEmail(userModel.getEmail());
            user.setPassword(userModel.getPassword());

            log.info("Saving new user to database: {}", userModel.getEmail());
            User savedUser = userRegisterRepository.save(user);

            log.info("User registered successfully: {} with ID: {}", userModel.getEmail(), savedUser.getId());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(true, ExceptionMessage.REGISTERED_SUCCESS, null));

        } catch (ApexCollectiveException e) {
            log.error("Registration failed with business exception for email {}: {}",
                    userModel.getEmail(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Registration failed with unexpected error for email {}: {}",
                    userModel.getEmail(), e.getMessage(), e);
            throw new ApexCollectiveException(ExceptionMessage.REGISTERED_FAILED, e);
        }
    }
}