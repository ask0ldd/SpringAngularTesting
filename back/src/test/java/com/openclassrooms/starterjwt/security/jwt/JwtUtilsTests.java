package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.DisplayName;
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

// 7 Tests

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

    // -------
    // GenerateJwt
    // -------

    @Test
    @DisplayName("when the principal is filled with the right user datas, .generateJwtToken should return a valid token")
    void testGenerateJwtToken_Success(){
        // Arrange
        UserDetails userDetails = new UserDetailsImpl(user1.getId(), user1.getEmail(), user1.getFirstName(), user1.getLastName(), user1.isAdmin(), user1.getPassword());
        when(authentication.getPrincipal()).thenReturn(userDetails);
        // Authentication authentication = new UsernamePasswordAuthenticationToken(user1Details, null);
        // Act
        String jwt = jwtUtils.generateJwtToken(authentication);
        // Assert
        assertThat(jwt).isNotNull();
        assertThat(jwtUtils.validateJwtToken(jwt)).isTrue();
        assertThat(jwtUtils.getUserNameFromJwtToken(jwt)).isEqualTo(user1.getEmail());
    }

    // -------
    // ValidateJwt
    // -------

    @Test
    @DisplayName("when a valid jwt is passed, .validateJwtToken should return true")
    void testValidateJwt_ExpectTrueWhenJwtIsValid(){
        // Arrange
        String validJwt = Jwts.builder()
            .setIssuer("self")
            .setSubject(user1.getEmail())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1)))
            .signWith(SignatureAlgorithm.HS512, this.jwtSecret)
            .compact();
        // Act
        Boolean validation = jwtUtils.validateJwtToken(validJwt);
        // Assert
        assertThat(validation).isTrue();
    }

    @Test
    @DisplayName("when a malformed jwt is passed, .validateJwtToken should return false")
    void testValidateJwt_ExpectFalseWhenJwtIsMalformed(){
        // Arrange
        String malformedJwt = "InvalidJwt";
        // Act
        Boolean validation = jwtUtils.validateJwtToken(malformedJwt);
        // Assert
        assertThat(validation).isFalse();
    }

    @Test
    @DisplayName("when a jwt with an invalid signature is passed, .validateJwtToken should return false")
    void testValidateJwt_ExpectFalseWhenSignatureIsInvalid(){
        // Arrange
        String falseSignatureJwt = Jwts.builder()
                .setIssuer("self")
                .setSubject(user1.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date())
                .signWith(SignatureAlgorithm.HS512, "falseSecret")
                .compact();
        // Act
        Boolean validation = jwtUtils.validateJwtToken(falseSignatureJwt);
        // Assert
        assertThat(validation).isFalse();
    }

    @Test
    @DisplayName("when an expired jwt is passed, .validateJwtToken should return false")
    void testValidateJwt_ExpectFalseWhenJwtIsExpired(){
        // Arrange
        String expiredJwt = Jwts.builder()
            .setIssuer("self")
            .setSubject(user1.getEmail())
            .setIssuedAt(new Date())
            .setExpiration(new Date())
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();
        // Act
        Boolean validation = jwtUtils.validateJwtToken(expiredJwt);
        // Assert
        assertThat(validation).isFalse();
    }

    @Test
    @DisplayName("when an unsigned jwt is passed, .validateJwtToken should return false")
    void testValidateJwt_ExpectFalseWhenJwtIsUnsigned(){
        // Arrange
        String unsignedJwt = Jwts.builder()
                .setIssuer("self")
                .setSubject(user1.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1)))
                .compact();
        // Act
        Boolean validation = jwtUtils.validateJwtToken(unsignedJwt);
        // Assert
        assertThat(validation).isFalse();
    }

    // -------
    // UserNameFromJwt
    // -------

    @Test
    @DisplayName("when a valid jwt is passed, .getUserNameFromJwtToken should return a valid email")
    void testGetUserNameFromJwt_Success(){
        // Arrange
        String validJwt = Jwts.builder()
                .setIssuer("self")
                .setSubject(user1.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1)))
                .signWith(SignatureAlgorithm.HS512, this.jwtSecret)
                .compact();
        // Act
        String userNameAkaEmail = jwtUtils.getUserNameFromJwtToken(validJwt);
        // Assert
        assertThat(userNameAkaEmail).isEqualTo(user1.getEmail());
    }
}