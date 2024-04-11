package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class JwtUtilsTests {
    @Autowired
    JwtUtils jwtUtils;

    @MockBean
    Authentication authentication;

    @Value("${oc.app.jwtSecret}")
    private String jwtSecret;

    private final User user1 = User.builder().id(1L).admin(true).email("ced@ced.com").firstName("john").lastName("doe").password("aeazezeaeazeae").build();

    @Test
    void testGenerateJwtToken_Success(){
        UserDetails userDetails = new UserDetailsImpl(user1.getId(), user1.getEmail(), user1.getFirstName(), user1.getLastName(), user1.isAdmin(), user1.getPassword());
        when(authentication.getPrincipal()).thenReturn(userDetails);
        // Authentication authentication = new UsernamePasswordAuthenticationToken(user1Details, null);
        String jwt = jwtUtils.generateJwtToken(authentication);
        assertThat(jwt).isNotNull();
        assertThat(jwtUtils.validateJwtToken(jwt)).isTrue();
        assertThat(jwtUtils.getUserNameFromJwtToken(jwt)).isEqualTo(user1.getEmail());
    }

    @Test
    void testValidateJwt_ExpectTrueWhenJwtIsValid(){
        String validJwt = Jwts.builder()
            .setIssuer("self")
            .setSubject(user1.getEmail())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1)))
            .signWith(SignatureAlgorithm.HS512, this.jwtSecret)
            .compact();
        Boolean validation = jwtUtils.validateJwtToken(validJwt);
        assertThat(validation).isTrue();
    }

    @Test
    void testValidateJwt_ExpectFalseWhenJwtIsMalformed(){
        String malformedJwt = "InvalidJwt";
        Boolean validation = jwtUtils.validateJwtToken(malformedJwt);
        assertThat(validation).isFalse();
    }

    @Test
    void testValidateJwt_ExpectFalseWhenSignatureIsInvalid(){
        String falseSignatureJwt = Jwts.builder()
                .setIssuer("self")
                .setSubject(user1.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date())
                .signWith(SignatureAlgorithm.HS512, "falseSecret")
                .compact();
        Boolean validation = jwtUtils.validateJwtToken(falseSignatureJwt);
        assertThat(validation).isFalse();
    }

    @Test
    void testValidateJwt_ExpectFalseWhenJwtIsExpired(){
        String expiredJwt = Jwts.builder()
            .setIssuer("self")
            .setSubject(user1.getEmail())
            .setIssuedAt(new Date())
            .setExpiration(new Date())
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();
        Boolean validation = jwtUtils.validateJwtToken(expiredJwt);
        assertThat(validation).isFalse();
    }

    @Test
    void testValidateJwt_ExpectFalseWhenJwtIsUnsigned(){
        String unsignedJwt = Jwts.builder()
                .setIssuer("self")
                .setSubject(user1.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1)))
                .compact();
        Boolean validation = jwtUtils.validateJwtToken(unsignedJwt);
        assertThat(validation).isFalse();
    }

    @Test
    void getUserNameFromJwt_Success(){
        String validJwt = Jwts.builder()
                .setIssuer("self")
                .setSubject(user1.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1)))
                .signWith(SignatureAlgorithm.HS512, this.jwtSecret)
                .compact();
        String userNameAkaEmail = jwtUtils.getUserNameFromJwtToken(validJwt);
        assertThat(userNameAkaEmail).isEqualTo(user1.getEmail());
    }
}