package ApexCollectiveHibernateBE.ApexCollective.Controllers;


import ApexCollectiveHibernateBE.ApexCollective.Common.ApexCollectiveException;
import ApexCollectiveHibernateBE.ApexCollective.JwtConfiguration.JwtUtility;
import ApexCollectiveHibernateBE.ApexCollective.Model.LoginModel;
import ApexCollectiveHibernateBE.ApexCollective.Model.TokenModel;
import ApexCollectiveHibernateBE.ApexCollective.Services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth/login")
public class LoginUser {

    @Autowired
    private LoginService loginService;

    @Autowired
    private JwtUtility jwtUtility;

    @PostMapping("/user")
    public TokenModel getUser(@RequestBody LoginModel loginModel) throws ApexCollectiveException {
        return loginService.loginUser(loginModel);
    }

    @GetMapping("/verify")
    public ResponseEntity<Map<String, Object>> verifyToken(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            Map<String, Object> response = new HashMap<>();
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                response.put("valid", false);
                response.put("message", "Missing or invalid Authorization header");
                return ResponseEntity.badRequest().body(response);
            }
            String token = authorizationHeader.substring(7);
            boolean isValid = jwtUtility.validateJwtToken(token);
            if (isValid) {
                response.put("valid", true);
                response.put("message", "Token is valid");
                String username = jwtUtility.getUserNameFromJwtToken(token);
                response.put("username", username);
                return ResponseEntity.ok(response);
            } else {
                response.put("valid", false);
                response.put("message", "Token is invalid or expired");
                return ResponseEntity.status(401).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("valid", false);
            errorResponse.put("message", "Token verification failed: " + e.getMessage());
            return ResponseEntity.status(401).body(errorResponse);
        }
    }



}
