package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

// 6 Tests

@SpringBootTest
public class UserMapperTests {
    @InjectMocks
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    public void shouldMapUserDtoToUserEntity() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setEmail("ced@ced.com");
        userDto.setPassword("password");
        // Act
        User user = userMapper.toEntity(userDto);
        // Assert
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(userDto.getId());
        assertThat(user.getFirstName()).isEqualTo(userDto.getFirstName());
        assertThat(user.getLastName()).isEqualTo(userDto.getLastName());
    }

    @Test
    void shouldMapUserEntityToDto() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("ced@ced.com");
        user.setPassword("password");
        // Act
        UserDto userDto = userMapper.toDto(user);
        // Assert
        assertThat(userDto).isNotNull();
        assertThat(user.getId()).isEqualTo(userDto.getId());
        assertThat(user.getFirstName()).isEqualTo(userDto.getFirstName());
        assertThat(user.getLastName()).isEqualTo(userDto.getLastName());
    }

    @Test
    void shouldReturnNull_WhenMappingNullDto() {
        // Act
        User user = userMapper.toEntity((UserDto) null);
        // Assert
        assertThat(user).isNull();
    }

    @Test
    void shouldReturnNull_WhenMappingNullEntity() {
        // Act
        UserDto userDto = userMapper.toDto((User) null);
        // Assert
        assertThat(userDto).isNull();
    }

    @Test
    void testMapToEntityList() {
        // Arrange
        List<UserDto> userDtoList = new ArrayList<>();
        UserDto userDto1 = new UserDto();
        userDto1.setId(1L);
        userDto1.setFirstName("John");
        userDto1.setLastName("Doe");
        userDto1.setEmail("ced@ced.com");
        userDto1.setPassword("password");
        UserDto userDto2 = new UserDto();
        userDto2.setId(2L);
        userDto2.setFirstName("Jane");
        userDto2.setLastName("Doe");
        userDto2.setEmail("ced2@ced.com");
        userDto2.setPassword("password2");
        userDtoList.add(userDto1);
        userDtoList.add(userDto2);
        // Act
        List<User> userList = userMapper.toEntity(userDtoList);
        // Assert
        assertThat(userList).isNotNull();
        assertThat(userList.size()).isEqualTo(2);
        assertThat(userList.get(0).getId()).isEqualTo(userDto1.getId());
        assertThat(userList.get(1).getId()).isEqualTo(userDto2.getId());
        assertThat(userList.get(0).getFirstName()).isEqualTo(userDto1.getFirstName());
        assertThat(userList.get(1).getFirstName()).isEqualTo(userDto2.getFirstName());
        assertThat(userList.get(0).getLastName()).isEqualTo(userDto1.getLastName());
        assertThat(userList.get(1).getLastName()).isEqualTo(userDto2.getLastName());
    }

    @Test
    void testMapToDtoList() {
        // Arrange
        List<User> userList = new ArrayList<>();
        User user1 = new User();
        user1.setId(1L);
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("ced@ced.com");
        user1.setPassword("password");
        User user2 = new User();
        user2.setId(2L);
        user2.setFirstName("Jane");
        user2.setLastName("Doe");
        user2.setEmail("ced2@ced.com");
        user2.setPassword("password2");
        userList.add(user1);
        userList.add(user2);
        // Act
        List<UserDto> userDtoList = userMapper.toDto(userList);
        // Assert
        assertThat(userDtoList).isNotNull();
        assertThat(userDtoList.size()).isEqualTo(2);
        assertThat(userDtoList.get(0).getId()).isEqualTo(user1.getId());
        assertThat(userDtoList.get(1).getId()).isEqualTo(user2.getId());
        assertThat(userDtoList.get(0).getFirstName()).isEqualTo(user1.getFirstName());
        assertThat(userDtoList.get(1).getFirstName()).isEqualTo(user2.getFirstName());
        assertThat(userDtoList.get(0).getLastName()).isEqualTo(user1.getLastName());
        assertThat(userDtoList.get(1).getLastName()).isEqualTo(user2.getLastName());
    }

}
