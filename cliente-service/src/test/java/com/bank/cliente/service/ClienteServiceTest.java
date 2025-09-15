package com.bank.cliente.service;

import com.bank.cliente.dto.ClienteDTO;
import com.bank.cliente.entity.Cliente;
import com.bank.cliente.exception.ClienteAlreadyExistsException;
import com.bank.cliente.exception.ClienteNotFoundException;
import com.bank.cliente.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;
    private ClienteDTO clienteDTO;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Jose Lema");
        cliente.setGenero("Masculino");
        cliente.setEdad(30);
        cliente.setIdentificacion("1234567890");
        cliente.setDireccion("Otavalo sn y principal");
        cliente.setTelefono("098254785");
        cliente.setClienteId("JOSE001");
        cliente.setContrasena("1234");
        cliente.setEstado(true);

        clienteDTO = new ClienteDTO();
        clienteDTO.setNombre("Jose Lema");
        clienteDTO.setGenero("Masculino");
        clienteDTO.setEdad(30);
        clienteDTO.setIdentificacion("1234567890");
        clienteDTO.setDireccion("Otavalo sn y principal");
        clienteDTO.setTelefono("098254785");
        clienteDTO.setClienteId("JOSE001");
        clienteDTO.setContrasena("1234");
        clienteDTO.setEstado(true);
    }

    @Test
    void testFindByIdSuccess() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        ClienteDTO result = clienteService.findById(1L);

        assertNotNull(result);
        assertEquals("Jose Lema", result.getNombre());
        assertEquals("JOSE001", result.getClienteId());
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ClienteNotFoundException.class, () -> clienteService.findById(1L));
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateClienteSuccess() {
        when(clienteRepository.existsByClienteId("JOSE001")).thenReturn(false);
        when(clienteRepository.existsByIdentificacion("1234567890")).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        ClienteDTO result = clienteService.create(clienteDTO);

        assertNotNull(result);
        assertEquals("Jose Lema", result.getNombre());
        assertEquals("JOSE001", result.getClienteId());
        verify(clienteRepository, times(1)).existsByClienteId("JOSE001");
        verify(clienteRepository, times(1)).existsByIdentificacion("1234567890");
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void testCreateClienteAlreadyExistsByClienteId() {
        when(clienteRepository.existsByClienteId("JOSE001")).thenReturn(true);

        assertThrows(ClienteAlreadyExistsException.class, () -> clienteService.create(clienteDTO));
        verify(clienteRepository, times(1)).existsByClienteId("JOSE001");
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void testCreateClienteAlreadyExistsByIdentificacion() {
        when(clienteRepository.existsByClienteId("JOSE001")).thenReturn(false);
        when(clienteRepository.existsByIdentificacion("1234567890")).thenReturn(true);

        assertThrows(ClienteAlreadyExistsException.class, () -> clienteService.create(clienteDTO));
        verify(clienteRepository, times(1)).existsByClienteId("JOSE001");
        verify(clienteRepository, times(1)).existsByIdentificacion("1234567890");
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void testDeleteClienteSuccess() {
        when(clienteRepository.existsById(1L)).thenReturn(true);

        clienteService.delete(1L);

        verify(clienteRepository, times(1)).existsById(1L);
        verify(clienteRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteClienteNotFound() {
        when(clienteRepository.existsById(1L)).thenReturn(false);

        assertThrows(ClienteNotFoundException.class, () -> clienteService.delete(1L));
        verify(clienteRepository, times(1)).existsById(1L);
        verify(clienteRepository, never()).deleteById(1L);
    }
}
