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

/*
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock UserRepository userRepository;

    @InjectMocks UserService userService;

    @Test
    @DisplayName("When I findById valid user, it should return mock and call userRepo")
    public void testFindValidUserById() {
        //Given
        User mockUser=User.builder()
                .email("test@test.com")
                .firstName("Prenom")
                .lastName("Nom")
                .password("123456")
                .admin(true)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        //When
        User user=userService.findById(1L);

        //Then
        assertThat(user).isEqualTo(mockUser);
        verify(userRepository,times(1)).findById(1L);
    }

    @Test
    @DisplayName("When I delete user, it should call user repo")
    public void testDeleteExistingUserById() {
        //Given

        //When
        userService.delete(3L);

        //Then
        verify(userRepository,times(1)).deleteById(3L);
    }
}*/