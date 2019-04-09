package ua.nike.project.spring.service.auth;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nike.project.spring.vo.UserVO;

import java.util.*;

@Service
public class TokenServiceImpl {

    @Autowired
    private UserService userService;

    public TokenObject getToken(UserVO userVO) throws Exception {
        if (userVO == null || userVO.getLogin() == null || userVO.getPassword() == null)
            return null;
        UserVO user = userService.loadUserByUsername(userVO.getLogin());
        Map<String, Object> tokenData = new HashMap<>();
        if (userVO.getPassword().equals(user.getPassword())) {
            tokenData.put("clientType", "user");
            tokenData.put("userID", user.getUserId().toString());
            tokenData.put("username", authorizedUser.getUsername());
            tokenData.put("token_create_date", new Date().getTime());
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, 100);
            tokenData.put("token_expiration_date", calendar.getTime());
            JwtBuilder jwtBuilder = Jwts.builder();
            jwtBuilder.setExpiration(calendar.getTime());
            jwtBuilder.setClaims(tokenData);
            String key = "abc123";
            String token = jwtBuilder.signWith(SignatureAlgorithm.HS512, key).compact();
            return token;
        } else {
            throw new Exception("Authentication error");
        }
    }
}
