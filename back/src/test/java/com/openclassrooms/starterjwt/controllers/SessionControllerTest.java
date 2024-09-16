package com.openclassrooms.starterjwt.controllers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.Matchers.*;
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

    private User existingUser;

    private List<User> list;

    private Teacher existingTeacher;

    private Session existingSession;

    @BeforeEach
    public void clean() {
        // Clean up before each test
        sessionRepository.deleteAll();
    userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testFindByIdSuccess() throws Exception {
        //Given
        existingUser = User.builder()
                .email("testuser@example.com")
                .firstName("User")
                .lastName("Test")
                .password("password123")
                .admin(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        list = Collections.singletonList(existingUser);


        existingTeacher = Teacher.builder()
                .firstName("User")
                .lastName("Test")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        existingSession = Session.builder()
                .name("Yoga")
                .date(new Date())
                .description("Nouvelle session")
                .teacher(existingTeacher)
                .users(list)
                .build();
        userRepository.save(existingUser);
        teacherRepository.save(existingTeacher);

        Session savedSession = sessionRepository.save(existingSession);
        //When
        mockMvc.perform(get("/api/session/{id}", savedSession.getId()))
        //Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedSession.getId().intValue()))
                .andExpect(jsonPath("$.name").value("Yoga"))
                .andExpect(jsonPath("$.description").value("Nouvelle session"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testFindByIdFail() throws Exception {
        //Given
        existingUser = User.builder()
                .email("testuser@example.com")
                .firstName("User")
                .lastName("Test")
                .password("password123")
                .admin(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        list = Collections.singletonList(existingUser);


        existingTeacher = Teacher.builder()
                .firstName("User")
                .lastName("Test")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        existingSession = Session.builder()
                .name("Yoga")
                .date(new Date())
                .description("Nouvelle session")
                .teacher(existingTeacher)
                .users(list)
                .build();
        userRepository.save(existingUser);
        teacherRepository.save(existingTeacher);

        Session savedSession = sessionRepository.save(existingSession);
        //When
        mockMvc.perform(get("/api/session/{id}", 999))
        //Then
                .andExpect(status().isNotFound());
    }



    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testFindAllSuccess() throws Exception {
        //Given
        existingUser = User.builder()
                .email("testuser@example.com")
                .firstName("User")
                .lastName("Test")
                .password("password123")
                .admin(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        list = Collections.singletonList(existingUser);


        existingTeacher = Teacher.builder()
                .firstName("User")
                .lastName("Test")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        existingSession = Session.builder()
                .name("Yoga")
                .date(new Date())
                .description("Nouvelle session")
                .teacher(existingTeacher)
                .users(list)
                .build();
        userRepository.save(existingUser);
        teacherRepository.save(existingTeacher);

        Session savedSession = sessionRepository.save(existingSession);
        //When
        mockMvc.perform(get("/api/session/"))
        //Then
                .andExpect(status().isOk())
         .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Yoga")));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testCreateSessionSuccess() throws Exception {
        //Given
        existingUser = User.builder()
                .email("testuser@example.com")
                .firstName("User")
                .lastName("Test")
                .password("password123")
                .admin(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        list = Collections.singletonList(existingUser);


        existingTeacher = Teacher.builder()
                .firstName("User")
                .lastName("Test")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        existingSession = Session.builder()
                .name("Yoga")
                .date(new Date())
                .description("Nouvelle session")
                .teacher(existingTeacher)
                .users(list)
                .build();
        userRepository.save(existingUser);
        teacherRepository.save(existingTeacher);

        Session newSession = Session.builder()
                .name("Pilates")
                .date(new Date())
                .description("Nouvelle session de Pilates")
                .teacher(existingTeacher)
                .users(Collections.singletonList(existingUser))
                .build();
        //When
        mockMvc.perform(post("/api/session/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionMapper.toDto(newSession))))
        //Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pilates"))
                .andExpect(jsonPath("$.description").value("Nouvelle session de Pilates"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testUpdateSessionSuccess() throws Exception {
        //Given
        existingUser = User.builder()
                .email("testuser@example.com")
                .firstName("User")
                .lastName("Test")
                .password("password123")
                .admin(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        list = Collections.singletonList(existingUser);


        existingTeacher = Teacher.builder()
                .firstName("User")
                .lastName("Test")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        existingSession = Session.builder()
                .name("Yoga")
                .date(new Date())
                .description("Nouvelle session")
                .teacher(existingTeacher)
                .users(list)
                .build();
        userRepository.save(existingUser);
        teacherRepository.save(existingTeacher);
        Session savedSession = sessionRepository.save(existingSession);

        savedSession.setName("Updated Yoga");
        savedSession.setDescription("Updated description");
        //When
        mockMvc.perform(put("/api/session/{id}", savedSession.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionMapper.toDto(savedSession))))
        //Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Yoga"))
                .andExpect(jsonPath("$.description").value("Updated description"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testDeleteSessionSuccess() throws Exception {
        //Given
        existingUser = User.builder()
                .email("testuser@example.com")
                .firstName("User")
                .lastName("Test")
                .password("password123")
                .admin(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        list = Collections.singletonList(existingUser);


        existingTeacher = Teacher.builder()
                .firstName("User")
                .lastName("Test")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        existingSession = Session.builder()
                .name("Yoga")
                .date(new Date())
                .description("Nouvelle session")
                .teacher(existingTeacher)
                .users(list)
                .build();
        userRepository.save(existingUser);
        teacherRepository.save(existingTeacher);
        Session savedSession = sessionRepository.save(existingSession);
        //When
        mockMvc.perform(delete("/api/session/{id}", savedSession.getId()))
        //Then
                .andExpect(status().isOk());

        assertThat(sessionRepository.findById(savedSession.getId())).isEmpty();
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testParticipateInSessionSuccess() throws Exception {
        //Given
        existingUser = User.builder()
                .email("testuser@example.com")
                .firstName("User")
                .lastName("Test")
                .password("password123")
                .admin(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        list = Collections.singletonList(existingUser);


        existingTeacher = Teacher.builder()
                .firstName("User")
                .lastName("Test")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        existingSession = Session.builder()
                .name("Yoga")
                .date(new Date())
                .description("Nouvelle session")
                .teacher(existingTeacher)
                .users(list)
                .build();
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
        //When
        mockMvc.perform(post("/api/session/{id}/participate/{userId}", savedSession.getId(), newUser.getId()))
        //Then
                .andExpect(status().isOk());

        Session updatedSession = sessionRepository.findById(savedSession.getId()).orElse(null);
        assertThat(updatedSession.getUsers().size()).isEqualTo(2);
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testNoLongerParticipateInSessionSuccess() throws Exception {
        //Given
        existingUser = User.builder()
                .email("testuser@example.com")
                .firstName("User")
                .lastName("Test")
                .password("password123")
                .admin(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        list = Collections.singletonList(existingUser);


        existingTeacher = Teacher.builder()
                .firstName("User")
                .lastName("Test")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        existingSession = Session.builder()
                .name("Yoga")
                .date(new Date())
                .description("Nouvelle session")
                .teacher(existingTeacher)
                .users(list)
                .build();
        userRepository.save(existingUser);
        teacherRepository.save(existingTeacher);
        Session savedSession = sessionRepository.save(existingSession);
        //When
        mockMvc.perform(delete("/api/session/{id}/participate/{userId}", savedSession.getId(), existingUser.getId()))
        //Then
                .andExpect(status().isOk());

        Session updatedSession = sessionRepository.findById(savedSession.getId()).orElse(null);
        assertThat(updatedSession.getUsers().size()).isEqualTo(0);
    }
}

