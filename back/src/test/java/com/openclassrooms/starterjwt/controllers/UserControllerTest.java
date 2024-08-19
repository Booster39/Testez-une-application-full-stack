package com.openclassrooms.starterjwt.controllers;

import static org.junit.jupiter.api.Assertions.*;

import com.openclassrooms.starterjwt.models.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static com.openclassrooms.starterjwt.controllers.AuthControllerTest.asJsonString;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import jdk.jfr.Enabled;
import org.h2.engine.UserBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SessionMapper sessionMapper;

    private  User existingUser;

    @BeforeEach
    void clean(){
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testFindByIdSuccess() throws Exception {
        existingUser = User.builder()
                .email("testuser@example.com")
                .firstName("User")
                .lastName("Test")
                .password("password123")
                .admin(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        userRepository.save(existingUser);

        mockMvc.perform(get("/api/user/{id}", existingUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingUser.getId().intValue()))
                .andExpect(jsonPath("$.lastName").value("Test"));
    }


    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testFindByIdNotFound() throws Exception {
        existingUser = User.builder()
                .email("testuser@example.com")
                .firstName("User")
                .lastName("Test")
                .password("password123")
                .admin(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        mockMvc.perform(get("/api/user/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testFindByIdBadRequest() throws Exception {
        existingUser = User.builder()
                .email("testuser@example.com")
                .firstName("User")
                .lastName("Test")
                .password("password123")
                .admin(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        mockMvc.perform(get("/api/user/{id}", "invalidId"))
                .andExpect(status().isBadRequest());
    }



    @Test
    @WithMockUser(username = "testuser@example.com")
    public void testDeleteUserNotFound() throws Exception {
        existingUser = User.builder()
                .email("testuser@example.com")
                .firstName("User")
                .lastName("Test")
                .password("password123")
                .admin(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        mockMvc.perform(delete("/api/user/{id}", 999)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    @Test
    @WithMockUser(username = "testuser@example.com")
    public void testDeleteUserSuccess() throws Exception {
        existingUser = User.builder()
                .email("testuser@example.com")
                .firstName("User")
                .lastName("Test")
                .password("password123")
                .admin(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        // Save the user to the repository
        existingUser = userRepository.save(existingUser);

        // Perform the delete request
        mockMvc.perform(delete("/api/user/{id}", existingUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Verify user is actually deleted
        assertFalse(userRepository.findById(existingUser.getId()).isPresent());
    }

    @Test
    @WithMockUser(username = "anotheruser@example.com")
    public void testDeleteUserUnauthorized() throws Exception {
        existingUser = User.builder()
                .email("testuser@example.com")
                .firstName("User")
                .lastName("Test")
                .password("password123")
                .admin(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        // Save the user to the repository
        existingUser = userRepository.save(existingUser);

        // Perform the delete request
        mockMvc.perform(delete("/api/user/{id}", existingUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        // Verify the user still exists
        assertTrue(userRepository.findById(existingUser.getId()).isPresent());
    }
}
