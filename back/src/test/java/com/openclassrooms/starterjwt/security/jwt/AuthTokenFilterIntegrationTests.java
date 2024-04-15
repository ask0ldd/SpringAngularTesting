package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

// 2 Tests

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Sql(scripts = "classpath:sql/reset-database.sql")
public class AuthTokenFilterIntegrationTests {
    @Autowired
    AuthTokenFilter authTokenFilter;
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @MockBean
    JwtUtils jwtUtils;

    private final User user1 = User.builder().id(1L).admin(false).email("yoga@studio.com").firstName("user1Fn").lastName("user1Ln").password("$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq").build();

    private final UserDetailsImpl userDetails = new UserDetailsImpl(
            user1.getId(),
            user1.getEmail(),
            user1.getFirstName(),
            user1.getLastName(),
            user1.isAdmin(),
            user1.getPassword()
    );

    // -------
    // DoFilterInternal
    // -------

    @Test
    @DisplayName("when a valid jwt with a valid email is sent through the request, the security context holder should hold a valid principal")
    void testDoFilterInternalWithMockJwtUtils_ValidJwt() throws ServletException, IOException {
        // Arrange
        // UserDetails principal = new UserDetailsImpl(user1.getId(), user1.getEmail(), user1.getFirstName(), user1.getLastName(), user1.isAdmin(), user1.getPassword());
        when(jwtUtils.validateJwtToken(anyString())).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken(anyString())).thenReturn("yoga@studio.com");
        // Create a new HttpServletRequest with a fake valid Jwt
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + "JwtOverriddenByMockvalidateJwtToken");
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();
        MockFilterChain mockFilterChain = new MockFilterChain();
        // Act
        authTokenFilter.doFilterInternal(request, mockResponse, mockFilterChain);
        // Assert : user 1 should now be the principal
        UserDetails updatedPrincipal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        assertThat(updatedPrincipal.getUsername()).isEqualTo(user1.getEmail());
    }

    @Test
    @DisplayName("when a invalid jwt is sent through the request, the security context holder should hold no principal")
    void testDoFilterInternalWithMockJwtUtils_InvalidJwt() throws ServletException, IOException {
        // Arrange
        // UserDetails principal = new UserDetailsImpl(user1.getId(), user1.getEmail(), user1.getFirstName(), user1.getLastName(), user1.isAdmin(), user1.getPassword());
        when(jwtUtils.validateJwtToken(anyString())).thenReturn(false);
        // Create a new HttpServletRequest with a fake invalid Jwt
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + "JwtOverriddenByMockvalidateJwtToken");
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();
        MockFilterChain mockFilterChain = new MockFilterChain();
        // Act
        authTokenFilter.doFilterInternal(request, mockResponse, mockFilterChain);
        // Assert
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNull();
    }
}
