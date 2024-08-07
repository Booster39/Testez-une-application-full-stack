package com.openclassrooms.starterjwt.controllers;

import static com.openclassrooms.starterjwt.controllers.AuthControllerTest.asJsonString;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class SessionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SessionMapper sessionMapper;

    @BeforeEach
    public void setUp() {
        // Clean up before each test

    }
    Long id = 1L;
    User existingUser = User.builder()
            .email("testuser@example.com")
            .firstName("User")
            .lastName("Test")
            .password("password123")
            .admin(false)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    List<User> list = Collections.singletonList(existingUser);


    // Creating Teacher with builder
    Teacher existingTeacher = Teacher.builder()
            .firstName("User")
            .lastName("Test")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    // Creating Session with builder
    Session existingSession = Session.builder()
            .name("Yoga")
            .date(new Date())
            .description("Nouvelle session")
            .teacher(existingTeacher)
            .users(list)
            .build();
    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testFindByIdSuccess() throws Exception {

        userRepository.save(existingUser);
        teacherRepository.save(existingTeacher);

        Session savedSession = sessionRepository.save(existingSession);

        mockMvc.perform(get("/api/session/{id}", savedSession.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedSession.getId().intValue()))
                .andExpect(jsonPath("$.name").value("Yoga"))
                .andExpect(jsonPath("$.description").value("Nouvelle session"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testFindByIdFail() throws Exception {

        userRepository.save(existingUser);
        teacherRepository.save(existingTeacher);

        Session savedSession = sessionRepository.save(existingSession);

        mockMvc.perform(get("/api/session/{id}", 999))
                .andExpect(status().isNotFound());
    }



    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testFindAllSuccess() throws Exception {
        sessionRepository.deleteAll();
        userRepository.save(existingUser);
        teacherRepository.save(existingTeacher);

        Session savedSession = sessionRepository.save(existingSession);

        mockMvc.perform(get("/api/session/"))
                .andExpect(status().isOk())
         .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Yoga")));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testCreateSessionSuccess() throws Exception {
        userRepository.save(existingUser);
        teacherRepository.save(existingTeacher);

        Session newSession = Session.builder()
                .name("Pilates")
                .date(new Date())
                .description("Nouvelle session de Pilates")
                .teacher(existingTeacher)
                .users(Collections.singletonList(existingUser))
                .build();

        mockMvc.perform(post("/api/session/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionMapper.toDto(newSession))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pilates"))
                .andExpect(jsonPath("$.description").value("Nouvelle session de Pilates"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testUpdateSessionSuccess() throws Exception {
        userRepository.save(existingUser);
        teacherRepository.save(existingTeacher);
        Session savedSession = sessionRepository.save(existingSession);

        savedSession.setName("Updated Yoga");
        savedSession.setDescription("Updated description");

        mockMvc.perform(put("/api/session/{id}", savedSession.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionMapper.toDto(savedSession))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Yoga"))
                .andExpect(jsonPath("$.description").value("Updated description"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testDeleteSessionSuccess() throws Exception {
        userRepository.save(existingUser);
        teacherRepository.save(existingTeacher);
        Session savedSession = sessionRepository.save(existingSession);

        mockMvc.perform(delete("/api/session/{id}", savedSession.getId()))
                .andExpect(status().isOk());

        assertThat(sessionRepository.findById(savedSession.getId())).isEmpty();
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testParticipateInSessionSuccess() throws Exception {
        userRepository.save(existingUser);
        teacherRepository.save(existingTeacher);
        Session savedSession = sessionRepository.save(existingSession);

        User newUser = User.builder()
                .email("newuser@example.com")
                .firstName("New")
                .lastName("User")
                .password("password123")
                .admin(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        userRepository.save(newUser);

        mockMvc.perform(post("/api/session/{id}/participate/{userId}", savedSession.getId(), newUser.getId()))
                .andExpect(status().isOk());

        Session updatedSession = sessionRepository.findById(savedSession.getId()).orElse(null);
        assertThat(updatedSession.getUsers().size()).isEqualTo(2);
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testNoLongerParticipateInSessionSuccess() throws Exception {
        userRepository.save(existingUser);
        teacherRepository.save(existingTeacher);
        Session savedSession = sessionRepository.save(existingSession);

        mockMvc.perform(delete("/api/session/{id}/participate/{userId}", savedSession.getId(), existingUser.getId()))
                .andExpect(status().isOk());

        Session updatedSession = sessionRepository.findById(savedSession.getId()).orElse(null);
        assertThat(updatedSession.getUsers().size()).isEqualTo(0);
    }
}

