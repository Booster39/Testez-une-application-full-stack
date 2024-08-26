package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SessionService underTest;

    @Mock
    private Teacher teacher;

    @Mock
    private User user;

    private User user1;
    private User user2;
    private List<User> userList;
    private Session session;

    @Test
    void checkIfFindAll() {
        // Given

        // When
        underTest.findAll();

        // Then
        verify(sessionRepository).findAll();
    }

    @Test
    void checkIfCreated() {
        // Given
        user1 = User.builder()
                .id(1L)
                .email("user1@example.com")
                .lastName("Smith")
                .firstName("John")
                .password("password1")
                .admin(false)
                .build();

        user2 = User.builder()
                .id(2L)
                .email("user2@example.com")
                .lastName("Doe")
                .firstName("Jane")
                .password("password2")
                .admin(false)
                .build();

        userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);

        session = Session.builder()
                .id(1L)
                .teacher(teacher)
                .description("description")
                .name("Lisa")
                .users(userList)
                .date(new Date())
                .build();

        // When
        underTest.create(session);

        // Then
        verify(sessionRepository).save(eq(session));
    }

    @Test
    void checkIfDeleted() {
        // Given
        session = Session.builder()
                .id(1L)
                .teacher(teacher)
                .description("description")
                .name("Lisa")
                .date(new Date())
                .build();

        // When
        underTest.delete(session.getId());

        // Then
        verify(sessionRepository).deleteById(eq(session.getId()));
    }

    @Test
    void checkIfGetById() {
        // Given
        session = Session.builder()
                .id(1L)
                .teacher(teacher)
                .description("description")
                .name("Lisa")
                .date(new Date())
                .build();

        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));

        // When
        underTest.getById(session.getId());

        // Then
        verify(sessionRepository).findById(eq(session.getId()));
    }

    @Test
    void checkIfUpdated() {
        // Given
        session = Session.builder()
                .id(1L)
                .teacher(teacher)
                .description("description")
                .name("Lisa")
                .date(new Date())
                .build();

        // When
        underTest.update(1L, session);

        // Then
        verify(sessionRepository).save(eq(session));
    }

    @Test
    void checkIfParticipateReturnNotFoundException() {
        // Given
        session = Session.builder()
                .id(1L)
                .teacher(teacher)
                .description("description")
                .name("Lisa")
                .date(new Date())
                .build();

        Long sessionId = session.getId();
        long userId = user.getId();

        // When & Then
        assertThrows(NotFoundException.class, () -> underTest.participate(sessionId, userId));
    }

    @Test
    void checkIfParticipateBadRequestException() {
        // Given
        Long sessionId = 1L;
        Long userId = 2L;

        User mockUser = mock(User.class);
        when(mockUser.getId()).thenReturn(userId);

        List<User> users = Arrays.asList(mockUser);
        Session mockSession = mock(Session.class);
        when(mockSession.getUsers()).thenReturn(users);
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(mockSession));
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // When & Then
        assertThrows(BadRequestException.class, () -> underTest.participate(sessionId, userId));
    }

    @Test
    void checkIfParticipateAddUser() {

        user1 = User.builder()
                .id(1L)
                .email("user1@example.com")
                .lastName("Smith")
                .firstName("John")
                .password("password1")
                .admin(false)
                .build();

        user2 = User.builder()
                .id(2L)
                .email("user2@example.com")
                .lastName("Doe")
                .firstName("Jane")
                .password("password2")
                .admin(false)
                .build();

        userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);

        session = Session.builder()
                .id(1L)
                .teacher(teacher)
                .description("description")
                .name("Lisa")
                .users(userList)
                .date(new Date())
                .build();
        Long sessionId = 1L;
        Long userId = 2L;
        List<User> voidList = new ArrayList<User>();

        User mockUser = mock(User.class);

        List<User> users = Arrays.asList(mockUser);
        Session mockSession = mock(Session.class);
        when(mockSession.getUsers()).thenReturn(users);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(mockSession));
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(mockSession.getUsers()).thenReturn(voidList);
        underTest.participate(sessionId, userId);

        when(mockSession.getUsers()).thenReturn(users);
        verify(sessionRepository).save(mockSession);
        assertTrue(mockSession.getUsers().contains(mockUser));
    }

    @Test
    void checkIfNoLongerParticipateThrowsNotFoundException() {
        // Given
        Long sessionId = 1L;
        long userId = user.getId();

        // When & Then
        assertThrows(NotFoundException.class, () -> underTest.noLongerParticipate(sessionId, userId));
    }

    @Test
    void checkIfNoLongerParticipateBadRequestException() {
        // Given
        Long sessionId = 1L;
        Long userId = 2L;

        User mockUser = mock(User.class);
        when(mockUser.getId()).thenReturn(userId + 1);

        List<User> users = Arrays.asList(mockUser);
        Session mockSession = mock(Session.class);
        when(mockSession.getUsers()).thenReturn(users);
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(mockSession));

        // When & Then
        assertThrows(BadRequestException.class, () -> underTest.noLongerParticipate(sessionId, userId));
    }

}
