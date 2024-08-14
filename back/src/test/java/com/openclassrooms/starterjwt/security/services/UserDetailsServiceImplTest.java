package com.openclassrooms.starterjwt.security.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {
    @Mock
    private UserRepository userRepository;


    private UserDetailsServiceImpl underTest;

    private String username;
    private User user;

    @BeforeEach
    void setUp()
    {
         username = "okba@gmail.com";
         user = User.builder()
                .id(1L)
                .admin(true)
                .email(username)
                .firstName("Okba")
                .lastName("Nafi")
                .password("pwd")
                .build();
        underTest = new UserDetailsServiceImpl(userRepository);
    }

    @Test
    void checkIfLoadUserByUsernameThrowsUsernameNotFoundException() {
        given(userRepository.findByEmail(username)).willReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> underTest.loadUserByUsername(username),
                "User Not Found with email: " + username);    }

    @Test
    void checkIfLoadUserByUsername() {
        given(userRepository.findByEmail(username)).willReturn(Optional.of(user));

        // Act
        underTest.loadUserByUsername(username);

        // Assert
        verify(userRepository).findByEmail(username);
    }
}