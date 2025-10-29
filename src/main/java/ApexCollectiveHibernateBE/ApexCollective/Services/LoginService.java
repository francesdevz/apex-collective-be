package ApexCollectiveHibernateBE.ApexCollective.Services;

import ApexCollectiveHibernateBE.ApexCollective.Common.ApexCollectiveException;
import ApexCollectiveHibernateBE.ApexCollective.Model.LoginModel;
import ApexCollectiveHibernateBE.ApexCollective.Model.TokenModel;

public interface LoginService {
    public TokenModel loginUser(LoginModel loginModel) throws ApexCollectiveException;
}
