package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class) // allow autorwired + clean context after each test
@SpringBootTest
public class AuthTokenFilterTests {
    @Autowired
    AuthTokenFilter authTokenFilter;
    @MockBean
    UserDetailsServiceImpl userDetailsService;
    @Autowired
    JwtUtils jwtUtils;
    @MockBean
    Authentication authentication;

    private final User user1 = User.builder().id(1L).admin(false).email("user1@ced.com").firstName("user1Fn").lastName("user1Ln").password("aeazezeaeazeae").build();

    private UserDetailsImpl userDetails = new UserDetailsImpl(
            user1.getId(),
            user1.getEmail(),
            user1.getFirstName(),
            user1.getLastName(),
            user1.isAdmin(),
            user1.getPassword()
    );

    @Test
    void testDoFilterInternal_ValidJwt() throws ServletException, IOException {
        // Arrange
        // Obtain a valid JWT token
        UserDetails userDetails = new UserDetailsImpl(user1.getId(), user1.getEmail(), user1.getFirstName(), user1.getLastName(), user1.isAdmin(), user1.getPassword());
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        String jwtToken = jwtUtils.generateJwtToken(authentication);
        // Create a new HttpServletRequest with the JWT token generated
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + jwtToken);
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();
        MockFilterChain mockFilterChain = new MockFilterChain();
        // Act
        authTokenFilter.doFilterInternal(request, mockResponse, mockFilterChain);
        // Assert : user 1 should now be the principal
        UserDetails updatedPrincipal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        assertThat(updatedPrincipal.getUsername()).isEqualTo(user1.getEmail());
    }

    @Test
    void testDoFilterInternal_NoJwt() throws ServletException, IOException {
        // Arrange
        // Create a new HttpServletRequest with no jwt in the header
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();
        MockFilterChain mockFilterChain = new MockFilterChain();
        // Act
        // assertThrows(Exception.class, () -> authTokenFilter.doFilterInternal(request, mockResponse, mockFilterChain));
        authTokenFilter.doFilterInternal(request, mockResponse, mockFilterChain);
        UserDetails updatedPrincipal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        assertThat(updatedPrincipal.getUsername()).isNull();
    }
}

/*

@Autowired
  private JwtUtils jwtUtils;

  @Autowired
  private UserDetailsServiceImpl userDetailsService;

  private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      String jwt = parseJwt(request);
      if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
        String username = jwtUtils.getUserNameFromJwtToken(jwt);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    } catch (Exception e) {
      logger.error("Cannot set user authentication: {}", e);
    }

    filterChain.doFilter(request, response);
  }

  private String parseJwt(HttpServletRequest request) {
    String headerAuth = request.getHeader("Authorization");

    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
      return headerAuth.substring(7, headerAuth.length());
    }

    return null;
  }

 */
