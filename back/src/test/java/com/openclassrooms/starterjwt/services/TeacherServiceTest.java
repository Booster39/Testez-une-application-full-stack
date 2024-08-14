package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {
@Mock
private TeacherRepository teacherRepository;

@InjectMocks
private TeacherService underTest;

private Teacher teacher;
    @BeforeEach
    void setUp() {
        teacherRepository.deleteAll();
        teacher = Teacher.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .build();

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
        when(teacherRepository.findById(1L)).thenReturn(java.util.Optional.of(teacher));

        // When
        Teacher teacherFromService = underTest.findById(1L);

        // Then
        assertThat(teacherFromService).isEqualTo(teacher);
        verify(teacherRepository, times(1)).findById(1L);
    }

}