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


    @Test
    void checkIfLoadUserByUsernameThrowsUsernameNotFoundException() {
        //Given
        user = User.builder()
                .id(1L)
                .admin(true)
                .email( "okba@gmail.com")
                .firstName("Okba")
                .lastName("Nafi")
                .password("pwd")
                .build();
        //When
        given(userRepository.findByEmail("okba@gmail.com")).willReturn(Optional.empty());
        //Then
        assertThrows(UsernameNotFoundException.class, () -> underTest.loadUserByUsername("okba@gmail.com"),
                "User Not Found with email: " + "okba@gmail.com");

    }

    @Test
    void checkIfLoadUserByUsername() {
        //Given
        user = User.builder()
                .id(1L)
                .admin(true)
                .email( "okba@gmail.com")
                .firstName("Okba")
                .lastName("Nafi")
                .password("pwd")
                .build();
        //When
        given(userRepository.findByEmail("okba@gmail.com")).willReturn(Optional.of(user));
        underTest.loadUserByUsername("okba@gmail.com");

        //Then
        verify(userRepository).findByEmail("okba@gmail.com");
    }
}