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