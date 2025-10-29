package ApexCollectiveHibernateBE.ApexCollective.Controllers;


import ApexCollectiveHibernateBE.ApexCollective.Common.ApiResponse;
import ApexCollectiveHibernateBE.ApexCollective.Model.UserModel;
import ApexCollectiveHibernateBE.ApexCollective.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/auth")
public class RegisterUser {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/register", produces = "application/json")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody UserModel userModel) {
       return userService.registerUser(userModel);
    }

}
