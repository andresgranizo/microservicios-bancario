package com.bank.cliente.service;

import com.bank.cliente.dto.ClienteDTO;
import com.bank.cliente.entity.Cliente;
import com.bank.cliente.exception.ClienteAlreadyExistsException;
import com.bank.cliente.exception.ClienteNotFoundException;
import com.bank.cliente.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<ClienteDTO> findAll() {
        return clienteRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ClienteDTO findById(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente no encontrado con ID: " + id));
        return convertToDTO(cliente);
    }

    public ClienteDTO findByClienteId(String clienteId) {
        Cliente cliente = clienteRepository.findByClienteId(clienteId)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente no encontrado con ID: " + clienteId));
        return convertToDTO(cliente);
    }

    public ClienteDTO create(ClienteDTO clienteDTO) {
        if (clienteRepository.existsByClienteId(clienteDTO.getClienteId())) {
            throw new ClienteAlreadyExistsException("Ya existe un cliente con ID: " + clienteDTO.getClienteId());
        }

        if (clienteRepository.existsByIdentificacion(clienteDTO.getIdentificacion())) {
            throw new ClienteAlreadyExistsException("Ya existe un cliente con identificación: " + clienteDTO.getIdentificacion());
        }

        Cliente cliente = convertToEntity(clienteDTO);
        Cliente savedCliente = clienteRepository.save(cliente);
        return convertToDTO(savedCliente);
    }

    public ClienteDTO update(Long id, ClienteDTO clienteDTO) {
        Cliente existingCliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente no encontrado con ID: " + id));

        // Verificar si el nuevo clienteId ya existe
        if (!existingCliente.getClienteId().equals(clienteDTO.getClienteId())
            && clienteRepository.existsByClienteId(clienteDTO.getClienteId())) {
            throw new ClienteAlreadyExistsException("Ya existe un cliente con ID: " + clienteDTO.getClienteId());
        }

        // Verificar si la nueva identificación ya existe
        if (!existingCliente.getIdentificacion().equals(clienteDTO.getIdentificacion())
            && clienteRepository.existsByIdentificacion(clienteDTO.getIdentificacion())) {
            throw new ClienteAlreadyExistsException("Ya existe un cliente con identificación: " + clienteDTO.getIdentificacion());
        }

        updateEntityFromDTO(existingCliente, clienteDTO);
        Cliente updatedCliente = clienteRepository.save(existingCliente);
        return convertToDTO(updatedCliente);
    }

    public void delete(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ClienteNotFoundException("Cliente no encontrado con ID: " + id);
        }
        clienteRepository.deleteById(id);
    }

    public List<ClienteDTO> findByEstado(Boolean estado) {
        return clienteRepository.findByEstado(estado).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ClienteDTO convertToDTO(Cliente cliente) {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(cliente.getId());
        dto.setNombre(cliente.getNombre());
        dto.setGenero(cliente.getGenero());
        dto.setEdad(cliente.getEdad());
        dto.setIdentificacion(cliente.getIdentificacion());
        dto.setDireccion(cliente.getDireccion());
        dto.setTelefono(cliente.getTelefono());
        dto.setClienteId(cliente.getClienteId());
        dto.setContrasena(cliente.getContrasena());
        dto.setEstado(cliente.getEstado());
        return dto;
    }

    private Cliente convertToEntity(ClienteDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNombre(dto.getNombre());
        cliente.setGenero(dto.getGenero());
        cliente.setEdad(dto.getEdad());
        cliente.setIdentificacion(dto.getIdentificacion());
        cliente.setDireccion(dto.getDireccion());
        cliente.setTelefono(dto.getTelefono());
        cliente.setClienteId(dto.getClienteId());
        cliente.setContrasena(dto.getContrasena());
        cliente.setEstado(dto.getEstado());
        return cliente;
    }

    private void updateEntityFromDTO(Cliente cliente, ClienteDTO dto) {
        cliente.setNombre(dto.getNombre());
        cliente.setGenero(dto.getGenero());
        cliente.setEdad(dto.getEdad());
        cliente.setIdentificacion(dto.getIdentificacion());
        cliente.setDireccion(dto.getDireccion());
        cliente.setTelefono(dto.getTelefono());
        cliente.setClienteId(dto.getClienteId());
        cliente.setContrasena(dto.getContrasena());
        cliente.setEstado(dto.getEstado());
    }
}
