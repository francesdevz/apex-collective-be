package ApexCollectiveHibernateBE.ApexCollective.ServiceImp;


import ApexCollectiveHibernateBE.ApexCollective.Common.ApexCollectiveException;
import ApexCollectiveHibernateBE.ApexCollective.Common.ApiResponse;
import ApexCollectiveHibernateBE.ApexCollective.Entity.User;
import ApexCollectiveHibernateBE.ApexCollective.Model.UserModel;
import ApexCollectiveHibernateBE.ApexCollective.Repositories.UserRegisterRepository;
import ApexCollectiveHibernateBE.ApexCollective.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserRegisterRepository userRegisterRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<ApiResponse> registerUser(UserModel userModel) throws ApexCollectiveException {
        try {

            Optional<User> isUserAlreadyExist = userRegisterRepository.findByEmail(userModel.getEmail());

            if(!isUserAlreadyExist.isEmpty()) {
                throw new ApexCollectiveException("Email is already existed");
            }

            userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));

            User t = new User();
            t.setFullName(userModel.getFullName());
            t.setEmail(userModel.getEmail());
            t.setPassword(userModel.getPassword());
            userRegisterRepository.save(t);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(true, "User registered successfully", null));

        } catch (Exception e) {
            throw new ApexCollectiveException("Registration Failed", e);
        }
    }
}
