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

    // ICI

/*
    //1
    @Test
    void checkUserDetailsImplProperties() {
        // Given
        user = User.builder()
                .id(1L)
                .admin(true)
                .email("okba@gmail.com")
                .firstName("Okba")
                .lastName("Nafi")
                .password("pwd")
                .build();

        // When
        given(userRepository.findByEmail("okba@gmail.com")).willReturn(Optional.of(user));
        UserDetails userDetails = underTest.loadUserByUsername("okba@gmail.com");

        // Then
        assertNotNull(userDetails);
        assertEquals(user.getId(), ((UserDetailsImpl) userDetails).getId());
        assertEquals(user.getEmail(), userDetails.getUsername());
        assertEquals(user.getFirstName(), ((UserDetailsImpl) userDetails).getFirstName());
        assertEquals(user.getLastName(), ((UserDetailsImpl) userDetails).getLastName());
        assertEquals(user.getPassword(), userDetails.getPassword());
        assertTrue(userDetails.isEnabled());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
    }


    //2
    @Test
    void checkUserDetailsImplEquality() {
        // Given
        UserDetailsImpl userDetails1 = UserDetailsImpl.builder()
                .id(1L)
                .username("okba@gmail.com")
                .firstName("Okba")
                .lastName("Nafi")
                .password("pwd")
                .admin(false)
                .build();

        UserDetailsImpl userDetails2 = UserDetailsImpl.builder()
                .id(1L)
                .username("okba@gmail.com")
                .firstName("Okba")
                .lastName("Nafi")
                .password("pwd")
                .admin(false)
                .build();

        // When & Then
        //assertEquals(userDetails1.equals(userDetails2), true);
        assertFalse(userDetails1.getAdmin());
        assertEquals(userDetails1, userDetails2);
    }

    @Test
    void checkUserDetailsImplInequality() {
        // Given
        UserDetailsImpl userDetails1 = UserDetailsImpl.builder()
                .id(1L)
                .username("okba@gmail.com")
                .firstName("Okba")
                .lastName("Nafi")
                .password("pwd")
                .admin(false)
                .build();

        UserDetailsImpl userDetails2 = UserDetailsImpl.builder()
                .id(2L)
                .username("other@gmail.com")
                .firstName("John")
                .lastName("Doe")
                .password("pwd123")
                .admin(false)
                .build();

        // When & Then
        assertNotEquals(userDetails1, userDetails2);
    }

    //3
    @Test
    void checkUserDetailsSecurityMethods() {
        // Given
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("okba@gmail.com")
                .firstName("Okba")
                .lastName("Nafi")
                .password("pwd")
                .admin(false)
                .build();

        // When & Then
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
    }


    //4
    @Test
    void checkUserDetailsAuthorities() {
        // Given
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("okba@gmail.com")
                .firstName("Okba")
                .lastName("Nafi")
                .password("pwd")
                .build();

        // When & Then
        assertNotNull(userDetails.getAuthorities());
        assertTrue(userDetails.getAuthorities().isEmpty());
    }

*/


}