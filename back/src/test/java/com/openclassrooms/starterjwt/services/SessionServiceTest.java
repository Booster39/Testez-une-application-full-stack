package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;
    private SessionService underTest;

    @Mock
    private Teacher teacher;

    @Mock
    private List<User> userList;

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
            .users(userList)
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
        ArgumentCaptor<Session> sessionArgumentCaptor = ArgumentCaptor.forClass(Session.class);
        verify(sessionRepository).save(sessionArgumentCaptor.capture());

        Session capturedSession = sessionArgumentCaptor.getValue();
        assertEquals(capturedSession, session);
    }


    @Test
    void checkIfDeleted() {
        //given

        sessionRepository.save(session);
        //when
        underTest.delete(session.getId());
        //then
        ArgumentCaptor<Long> sessionIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(sessionRepository).deleteById(sessionIdArgumentCaptor.capture());

        Long capturedIdSession = sessionIdArgumentCaptor.getValue();
        assertEquals(capturedIdSession, session.getId());
    }

    @Test
    void checkIfGetById() {
        //given
        sessionRepository.save(session);
        //when
        underTest.getById(session.getId());
        //then
        ArgumentCaptor<Long> sessionIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(sessionRepository).findById(sessionIdArgumentCaptor.capture());

        Long capturedIdSession = sessionIdArgumentCaptor.getValue();
        assertEquals(capturedIdSession, session.getId());
    }

    @Test
    void checkIfUpdated() {
        //given

        //when
        underTest.update(1L, session);
        //then
        ArgumentCaptor<Session> sessionArgumentCaptor = ArgumentCaptor.forClass(Session.class);
        verify(sessionRepository).save(sessionArgumentCaptor.capture());

        Session capturedSession = sessionArgumentCaptor.getValue();
        assertEquals(capturedSession, session);
    }

    @Test
    @Disabled
    void checkIfParticipate() {

    }

    @Test
    @Disabled
    void checkIfNoLongerParticipate() {

    }
}