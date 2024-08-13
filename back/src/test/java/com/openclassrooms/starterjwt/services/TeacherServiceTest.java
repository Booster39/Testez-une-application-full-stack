package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {
@Mock
private TeacherRepository teacherRepository;
private TeacherService underTest;
    @BeforeEach
    void setUp() {
        underTest = new TeacherService(teacherRepository);
    }

    @Test
    void checkIfFindAll() {
        //when
        underTest.findAll();
        //then
        verify(teacherRepository).findAll();
    }
    @Test
    public void testFindById() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacher.setCreatedAt(now);
        teacher.setUpdatedAt(now);

        when(teacherRepository.findById(1L)).thenReturn(java.util.Optional.of(teacher));

        // When
        Teacher teacherFromService = underTest.findById(1L);

        // Then
        assertEquals(1L, teacherFromService.getId());
        assertEquals("John", teacherFromService.getFirstName());
        assertEquals("Doe", teacherFromService.getLastName());
        assertEquals(now, teacherFromService.getCreatedAt());
        assertEquals(now, teacherFromService.getUpdatedAt());
    }

    @Test
    void checkIfFindById() {
        //given
        Teacher teacher = Teacher.builder()
                .id(1L)
                .firstName("Lae")
                .lastName("Woe")
                .build();
        teacherRepository.save(teacher);
        //when
        underTest.findById(teacher.getId());
        //then
        ArgumentCaptor<Long> teacherIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(teacherRepository).findById(teacherIdArgumentCaptor.capture());

        Long capturedIdTeacher = teacherIdArgumentCaptor.getValue();
        assertEquals(capturedIdTeacher, teacher.getId());
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