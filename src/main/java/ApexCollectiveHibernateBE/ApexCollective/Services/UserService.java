package ApexCollectiveHibernateBE.ApexCollective.Services;

import ApexCollectiveHibernateBE.ApexCollective.Common.ApexCollectiveException;
import ApexCollectiveHibernateBE.ApexCollective.Common.ApiResponse;
import ApexCollectiveHibernateBE.ApexCollective.Model.UserModel;
import org.springframework.http.ResponseEntity;

public interface UserService {
    public ResponseEntity<ApiResponse> registerUser(UserModel userModel) throws ApexCollectiveException;
}
