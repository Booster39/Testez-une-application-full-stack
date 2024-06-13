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

    @BeforeEach
    void setUp() {
        underTest = new UserDetailsServiceImpl(userRepository);
    }

    @Test
    void checkIfLoadUserByUsernameThrowsUsernameNotFoundException() {
        String username = "okba@gmail.com";
        assertThrows(UsernameNotFoundException.class, () -> underTest.loadUserByUsername(username), "User Not Found with email: " + username);
    }

    @Test
    void checkIfLoadUserByUsername() {
        String username = "okba@gmail.com";
        User user = User.builder()
                .id(1L)
                .admin(true)
                .email(username)
                .firstName("Okba")
                .lastName("Nafi")
                .password("pwd")
                .build();
        userRepository.save(user);
        given(userRepository.findByEmail(username)).willReturn(Optional.of(user));
        underTest.loadUserByUsername(username);

        ArgumentCaptor<String> userMailArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(userRepository).findByEmail(userMailArgumentCaptor.capture());

        String userMailCaptured = userMailArgumentCaptor.getValue();
        assertEquals(userMailCaptured, username);
    }
}