package com.openclassrooms.starterjwt.security.services;

import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class UserDetailsImplTests {

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
    void testAllUserAccountProperties(){
        assertThat(userDetails.isAccountNonExpired()).isTrue();
        assertThat(userDetails.isAccountNonLocked()).isTrue();
        assertThat(userDetails.isCredentialsNonExpired()).isTrue();
        assertThat(userDetails.isEnabled()).isTrue();
        assertThat(userDetails.getAdmin()).isEqualTo(user1.isAdmin());
        assertThat(userDetails.getAuthorities()).isNotNull();
    }

    @Test
    void testEquals(){
        assertThat(userDetails.equals(userDetails)).isTrue();
    }

    @Test
    void testEquals_NotEqual(){
        assertThat(userDetails.equals("userDetails")).isFalse();
    }

}

/*

UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

 */