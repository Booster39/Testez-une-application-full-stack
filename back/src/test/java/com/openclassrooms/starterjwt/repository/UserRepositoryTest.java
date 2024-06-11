package com.openclassrooms.starterjwt.repository;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository underTest;


    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    @Disabled
    void checkIfexistsByEmail() {
        //given
        String email = "jamila@gmail.com";
        User user =  User.builder().
                id(1L)
                .email(email)
                .lastName("Traore")
                .firstName("Jamila")
                .password("sa")
                .admin(true)
        .build();
        underTest.save(user);
        //when
        boolean expected = underTest.existsByEmail(email);
        //then
        assertTrue(expected);
    }

    @Test
    @Disabled
    void checkIfNotExistsByEmail() {
        //given
        String email = "jamila@gmail.com";
        //when
        boolean expected = underTest.existsByEmail(email);
        //then
        assertFalse(expected);
    }

    @Test
    @Disabled
    void checkIfFindByEmail() {
        //given
        String email = "jamila@gmail.com";
        //when
        Optional<User> student = underTest.findByEmail(email);
        //then
        assertTrue(student.isPresent());
    }

    @Test
    @Disabled
    void checkIfNotFindByEmail() {
        //given
        String email = "jamila@gmail.com";
        //when
        Optional<User> student = underTest.findByEmail(email);
        //then
        assertFalse(student.isEmpty());
    }
}