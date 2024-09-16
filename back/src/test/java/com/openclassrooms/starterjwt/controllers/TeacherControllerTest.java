package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
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


    private Teacher existingTeacher;

    @BeforeEach
    void clean(){
        sessionRepository.deleteAll();
        teacherRepository.deleteAll();
        userRepository.deleteAll();
    }
    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testFindByIdSuccess() throws Exception {
        //Given
        existingTeacher = Teacher.builder()
                .firstName("User")
                .lastName("Test")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        Teacher savedTeacher = teacherRepository.save(existingTeacher);
        //When
        mockMvc.perform(get("/api/teacher/{id}", savedTeacher.getId()))
        //Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedTeacher.getId().intValue()))
                .andExpect(jsonPath("$.firstName").value("User"))
                .andExpect(jsonPath("$.lastName").value("Test"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testFindByIdFail() throws Exception {
        //Given
        existingTeacher = Teacher.builder()
                .firstName("User")
                .lastName("Test")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        Teacher savedTeacher = teacherRepository.save(existingTeacher);
        //When
        mockMvc.perform(get("/api/teacher/{id}", 999))
        //Then
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testFindAllSuccess() throws Exception {
        //Given
        existingTeacher = Teacher.builder()
                .firstName("User")
                .lastName("Test")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        teacherRepository.save(existingTeacher);
        //When
        mockMvc.perform(get("/api/teacher/"))
        //Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is("User")));
    }
}