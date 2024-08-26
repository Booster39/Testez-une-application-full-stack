package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService underTest;

    private Teacher teacher;

    @Test
    void checkIfFindAll() {
        // Given
        teacher = Teacher.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .build();

        // When
        underTest.findAll();

        // Then
        verify(teacherRepository).findAll();
    }

    @Test
    void testFindById() {
        // Given
        teacher = Teacher.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .build();

        when(teacherRepository.findById(1L)).thenReturn(java.util.Optional.of(teacher));

        // When
        Teacher teacherFromService = underTest.findById(1L);

        // Then
        assertThat(teacherFromService).isEqualTo(teacher);
        verify(teacherRepository, times(1)).findById(1L);
    }
}
