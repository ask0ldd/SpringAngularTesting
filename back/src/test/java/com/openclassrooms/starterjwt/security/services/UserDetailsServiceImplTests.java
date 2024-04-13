package com.openclassrooms.starterjwt.security.services;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserDetailsServiceImplTests {

}

/*
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  UserRepository userRepository;

  UserDetailsServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + username));

    return UserDetailsImpl
            .builder()
            .id(user.getId())
            .username(user.getEmail())
            .lastName(user.getLastName())
            .firstName(user.getFirstName())
            .password(user.getPassword())
            .build();
  }

}
/.////////////////////

UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();


 */