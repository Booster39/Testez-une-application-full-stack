package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @BeforeEach
    void setUp(){

    }
    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testFindByIdSuccess() throws Exception {
        Teacher existingTeacher = Teacher.builder()
                .firstName("User")
                .lastName("Test")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Teacher savedTeacher = teacherRepository.save(existingTeacher);

        mockMvc.perform(get("/api/teacher/{id}", savedTeacher.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedTeacher.getId().intValue()))
                .andExpect(jsonPath("$.firstName").value("User"))
                .andExpect(jsonPath("$.lastName").value("Test"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testFindByIdFail() throws Exception {
        Teacher existingTeacher = Teacher.builder()
                .firstName("User")
                .lastName("Test")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        Teacher savedTeacher = teacherRepository.save(existingTeacher);

        mockMvc.perform(get("/api/teacher/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testFindAllSuccess() throws Exception {
        Teacher existingTeacher = Teacher.builder()
                .firstName("User")
                .lastName("Test")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        teacherRepository.deleteAll();
        sessionRepository.deleteAll();
        teacherRepository.save(existingTeacher);

        mockMvc.perform(get("/api/teacher/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is("User")));
    }
}
/*
*  @BeforeEach
    public void setUp() {
        // Clean up before each test
        userRepository.deleteAll();
    }
 @Test
    public void testFindByIdValid() throws Exception {
        // Given
        Teacher teacher = new Teacher(null, "John", "Doe", null, null);
        teacherRepository.save(teacher);

        // When
        mockMvc.perform(get("/api/teacher/" + teacher.getId())
                        .header("Authorization", "Bearer " + getAdminAccessToken()))
                // Then
                .andExpect(status().isOk());
    }
* */