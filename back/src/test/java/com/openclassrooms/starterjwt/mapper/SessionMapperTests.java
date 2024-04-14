package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

// 6 Tests

public class SessionMapperTests {
    @InjectMocks
    private SessionMapper sessionMapper = Mappers.getMapper(SessionMapper.class);

    @Test
    public void shouldMapSessionDtoToSessionEntity() {
        // Arrange
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("sessionName");
        sessionDto.setDescription("sessionDescription");
        // Act
        Session session = sessionMapper.toEntity(sessionDto);
        // Assert
        assertThat(session).isNotNull();
        assertThat(session.getId()).isEqualTo(sessionDto.getId());
        assertThat(session.getName()).isEqualTo(sessionDto.getName());
        assertThat(session.getDescription()).isEqualTo(sessionDto.getDescription());
    }

    @Test
    void shouldMapSessionEntityToDto() {
        // Arrange
        Session session = new Session();
        session.setId(1L);
        session.setName("sessionName");
        session.setDescription("sessionDescription");
        // Act
        SessionDto sessionDto = sessionMapper.toDto(session);
        // Assert
        assertThat(sessionDto).isNotNull();
        assertThat(session.getId()).isEqualTo(sessionDto.getId());
        assertThat(session.getName()).isEqualTo(sessionDto.getName());
        assertThat(session.getDescription()).isEqualTo(sessionDto.getDescription());
    }

    @Test
    void shouldReturnNullWhenMappingNullDto() {
        // Act
        Session session = sessionMapper.toEntity((SessionDto) null);
        // Assert
        assertThat(session).isNull();
    }

    @Test
    void shouldReturnNullWhenMappingNullEntity() {
        // Act
        SessionDto sessionDto = sessionMapper.toDto((Session) null);
        // Assert
        assertThat(sessionDto).isNull();
    }

    @Test
    void testMapToEntityList() {
        // Arrange
        List<SessionDto> sessionDtoList = new ArrayList<>();
        SessionDto sessionDto1 = new SessionDto();
        sessionDto1.setId(1L);
        sessionDto1.setName("sessionName");
        sessionDto1.setDescription("sessionDescription");
        SessionDto sessionDto2 = new SessionDto();
        sessionDto2.setId(2L);
        sessionDto2.setName("sessionName2");
        sessionDto2.setDescription("sessionDescription2");
        sessionDtoList.add(sessionDto1);
        sessionDtoList.add(sessionDto2);
        // Act
        List<Session> sessionList = sessionMapper.toEntity(sessionDtoList);
        // Assert
        assertThat(sessionList).isNotNull();
        assertThat(sessionList.size()).isEqualTo(2);
        assertThat(sessionList.get(0).getId()).isEqualTo(sessionDto1.getId());
        assertThat(sessionList.get(1).getId()).isEqualTo(sessionDto2.getId());
        assertThat(sessionList.get(0).getName()).isEqualTo(sessionDto1.getName());
        assertThat(sessionList.get(1).getName()).isEqualTo(sessionDto2.getName());
        assertThat(sessionList.get(0).getDescription()).isEqualTo(sessionDto1.getDescription());
        assertThat(sessionList.get(1).getDescription()).isEqualTo(sessionDto2.getDescription());
    }

    @Test
    void testMapToDtoList() {
        // Arrange
        List<Session> sessionList = new ArrayList<>();
        Session session1 = new Session();
        session1.setId(1L);
        session1.setName("sessionName");
        session1.setDescription("sessionDescription");
        Session session2 = new Session();
        session2.setId(2L);
        session2.setName("sessionName2");
        session2.setDescription("sessionDescription2");
        sessionList.add(session1);
        sessionList.add(session2);
        // Act
        List<SessionDto> sessionDtoList = sessionMapper.toDto(sessionList);
        // Assert
        assertThat(sessionDtoList).isNotNull();
        assertThat(sessionDtoList.size()).isEqualTo(2);
        assertThat(sessionDtoList.get(0).getId()).isEqualTo(session1.getId());
        assertThat(sessionDtoList.get(1).getId()).isEqualTo(session2.getId());
        assertThat(sessionDtoList.get(0).getName()).isEqualTo(session1.getName());
        assertThat(sessionDtoList.get(1).getName()).isEqualTo(session2.getName());
        assertThat(sessionDtoList.get(0).getDescription()).isEqualTo(session1.getDescription());
        assertThat(sessionDtoList.get(1).getDescription()).isEqualTo(session2.getDescription());
    }
}
