package com.pequenospasos.backend.service;

import com.pequenospasos.backend.entity.Educador;
import com.pequenospasos.backend.repository.EducadorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EducadorServiceTest {
    @Mock
    private EducadorRepository educadorRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private EducadorService educadorService;

    @Test
    void saveEducador_whenEmailAlreadyExists_shouldThrowException() {
        Educador educador = new Educador();
        educador.setEmail("test@pequenospasos.com");

        when(educadorRepository.existsByEmail("test@pequenospasos.com")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> educadorService.saveEducador(educador));
        assertEquals("El email ya está registrado.", ex.getMessage());

        verify(educadorRepository, never()).save(any(Educador.class));
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void saveEducador_whenValid_shouldSetRoleEncodePasswordAndSave() {
        // Arrange
        Educador educador = new Educador();
        educador.setEmail("ok@pequenospasos.com");
        educador.setPassword("1234");

        when(educadorRepository.existsByEmail("ok@pequenospasos.com")).thenReturn(false);
        when(passwordEncoder.encode("1234")).thenReturn("ENC_1234");
        when(educadorRepository.save(any(Educador.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Educador saved = educadorService.saveEducador(educador);

        // Assert
        assertNotNull(saved);
        assertEquals("EDUCADOR", saved.getTipoUsuario());
        assertEquals("ENC_1234", saved.getPassword());

        verify(educadorRepository).save(any(Educador.class));
        verify(passwordEncoder).encode("1234");
    }
}
