package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    private UserService underTest;

    @BeforeEach
    void setUp() {
        underTest = new UserService(userRepository);
    }

    String email = "jamila@gmail.com";
    User user = User.builder()
            .id(1L)
            .email(email)
            .lastName("Traore")
            .firstName("Jamila")
            .password("sa")
            .admin(true)
            .build();
    @Test
    void checkIfDeleted() {
        //given

        userRepository.save(user);
        //when
        underTest.delete(user.getId());
        //then
        ArgumentCaptor<Long> userIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(userRepository).deleteById(userIdArgumentCaptor.capture());

        Long capturedIdUser = userIdArgumentCaptor.getValue();
        assertEquals(capturedIdUser, user.getId());
    }

    @Test
    void checkIfFindById() {
        //given

        userRepository.save(user);
        //when
        underTest.findById(user.getId());
        //then
        ArgumentCaptor<Long> userIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(userRepository).findById(userIdArgumentCaptor.capture());

        Long capturedIdUser = userIdArgumentCaptor.getValue();
        assertEquals(capturedIdUser, user.getId());
    }

}