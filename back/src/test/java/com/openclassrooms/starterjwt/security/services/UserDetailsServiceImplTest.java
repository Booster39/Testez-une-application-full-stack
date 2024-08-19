package com.openclassrooms.starterjwt.security.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
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


    @InjectMocks
    private UserDetailsServiceImpl underTest;
    
    private User user;

    @BeforeEach
    void clean()
    {
         userRepository.deleteAll();
    }

    @Test
    void checkIfLoadUserByUsernameThrowsUsernameNotFoundException() {
        user = User.builder()
                .id(1L)
                .admin(true)
                .email( "okba@gmail.com")
                .firstName("Okba")
                .lastName("Nafi")
                .password("pwd")
                .build();
        given(userRepository.findByEmail("okba@gmail.com")).willReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> underTest.loadUserByUsername("okba@gmail.com"),
                "User Not Found with email: " + "okba@gmail.com");    }

    @Test
    void checkIfLoadUserByUsername() {
        user = User.builder()
                .id(1L)
                .admin(true)
                .email( "okba@gmail.com")
                .firstName("Okba")
                .lastName("Nafi")
                .password("pwd")
                .build();
        given(userRepository.findByEmail("okba@gmail.com")).willReturn(Optional.of(user));

        // Act
        underTest.loadUserByUsername("okba@gmail.com");

        // Assert
        verify(userRepository).findByEmail("okba@gmail.com");
    }
}