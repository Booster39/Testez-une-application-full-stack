package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.sun.jdi.LongValue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static com.fasterxml.jackson.databind.type.LogicalType.Array;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
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

   User user1 = User.builder()
            .id(1L)
            .email("user1@example.com")
        .lastName("Smith")
        .firstName("John")
        .password("password1")
        .admin(false).build();

    User user2 = User.builder()
            .id(2L)
            .email("user2@example.com")
            .lastName("Doe")
            .firstName("Jane")
            .password("password2")
            .admin(false).build();

    public List<User> userList = new ArrayList<User>();
    public static List<User> createUsers(List<User> userList, User user1, User user2) {
        userList.add(user1);
        userList.add(user2);
        return userList;
    }


    @Mock
    private Date date;

    @BeforeEach
    void setUp() {
        underTest = new SessionService(sessionRepository, userRepository);
    }

    Session session = Session.builder()
            .id(1L)
            .teacher(teacher)
            .description("description")
            .name("Lisa")
            .users(createUsers(userList, user1, user2))
            .date(date)
            .build();

    @Test
    void checkIfFindAll() {
        //when
        underTest.findAll();
        //then
        verify(sessionRepository).findAll();
    }

    @Test
    void checkIfCreated() {
        //given
        //when
        underTest.create(session);
        //then
        verify(sessionRepository).save(eq(session));
    }


    @Test
    void checkIfDeleted() {
        //given

        sessionRepository.save(session);
        //when
        underTest.delete(session.getId());
        //then
        verify(sessionRepository).deleteById(eq(session.getId()));
    }

    @Test
    void checkIfGetById() {
        //given
        sessionRepository.save(session);
        //when
        underTest.getById(session.getId());
        //then
        verify(sessionRepository).findById(eq(session.getId()));
    }

    @Test
    void checkIfUpdated() {
        //given

        //when
        underTest.update(1L, session);
        //then
        verify(sessionRepository).save(eq(session));
    }

    @Test
    void checkIfParticipateReturnNotFoundException() {
        //given
        sessionRepository.save(session);
        Long sessionId = session.getId();
        long userId = user.getId();

        assertThrows(NotFoundException.class, () -> underTest.participate(sessionId, userId));
    }

   @Test
    void checkIfParticipateBadRequestException() {
       Long sessionId = 1L;
       Long userId = 2L;

       User mockUser = mock(User.class);
       when(mockUser.getId()).thenReturn(userId);

       List<User> users = Arrays.asList(mockUser);
       Session mockSession = mock(Session.class);
       when(mockSession.getUsers()).thenReturn(users);
       when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(mockSession));
       when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
       assertThrows(BadRequestException.class, () -> underTest.participate(sessionId, userId));

   }

    @Test
    void checkIfParticipateAddUser() {
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
        //given
        sessionRepository.save(session);
        Long sessionId = session.getId();
        long userId = user.getId();
        // when
        //then
        assertThrows(NotFoundException.class, ()->{underTest.noLongerParticipate(sessionId, userId);});
    }

    @Test
    void checkIfNoLongerParticipateBadRequestException() {
        //given
        Long sessionId = 1L;
        Long userId = 2L;

        User mockUser = mock(User.class);
        when(mockUser.getId()).thenReturn(userId + 1);

        List<User> users = Arrays.asList(mockUser);
        Session mockSession = mock(Session.class);
        when(mockSession.getUsers()).thenReturn(users);
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(mockSession));
        assertThrows(BadRequestException.class, ()->{underTest.noLongerParticipate(sessionId, userId);});
    }

    @Test
    void checkIfNoLongerParticipateAddUser() {
        Long sessionId = 1L;
        Long userId = 2L;
        List<User> voidList = new ArrayList<User>();
        User mockUser = mock(User.class);
        when(mockUser.getId()).thenReturn(userId);

        List<User> users = Arrays.asList(mockUser);
        Session mockSession = mock(Session.class);
        when(mockSession.getUsers()).thenReturn(users);
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(mockSession));

        underTest.noLongerParticipate(sessionId, userId);

        when(mockSession.getUsers()).thenReturn(voidList);
        verify(sessionRepository).save(mockSession);
        assertFalse(mockSession.getUsers().contains(voidList));

    }
}