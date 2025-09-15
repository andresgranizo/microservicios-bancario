package com.bank.cuenta.service;

import com.bank.cuenta.dto.CuentaDTO;
import com.bank.cuenta.entity.Cuenta;
import com.bank.cuenta.exception.CuentaNotFoundException;
import com.bank.cuenta.repository.CuentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CuentaService {

    @Autowired
    private CuentaRepository cuentaRepository;

    public List<CuentaDTO> findAll() {
        return cuentaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CuentaDTO findById(Long id) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new CuentaNotFoundException("Cuenta no encontrada con ID: " + id));
        return convertToDTO(cuenta);
    }

    public CuentaDTO findByNumeroCuenta(String numeroCuenta) {
        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new CuentaNotFoundException("Cuenta no encontrada: " + numeroCuenta));
        return convertToDTO(cuenta);
    }

    public CuentaDTO create(CuentaDTO cuentaDTO) {
        if (cuentaRepository.existsByNumeroCuenta(cuentaDTO.getNumeroCuenta())) {
            throw new RuntimeException("Ya existe una cuenta con número: " + cuentaDTO.getNumeroCuenta());
        }

        Cuenta cuenta = convertToEntity(cuentaDTO);
        Cuenta savedCuenta = cuentaRepository.save(cuenta);
        return convertToDTO(savedCuenta);
    }

    public CuentaDTO update(Long id, CuentaDTO cuentaDTO) {
        Cuenta existingCuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new CuentaNotFoundException("Cuenta no encontrada con ID: " + id));

        if (!existingCuenta.getNumeroCuenta().equals(cuentaDTO.getNumeroCuenta()) 
            && cuentaRepository.existsByNumeroCuenta(cuentaDTO.getNumeroCuenta())) {
            throw new RuntimeException("Ya existe una cuenta con número: " + cuentaDTO.getNumeroCuenta());
        }

        updateEntityFromDTO(existingCuenta, cuentaDTO);
        Cuenta updatedCuenta = cuentaRepository.save(existingCuenta);
        return convertToDTO(updatedCuenta);
    }

    public void delete(Long id) {
        if (!cuentaRepository.existsById(id)) {
            throw new CuentaNotFoundException("Cuenta no encontrada con ID: " + id);
        }
        cuentaRepository.deleteById(id);
    }

    public List<CuentaDTO> findByClienteId(String clienteId) {
        return cuentaRepository.findByClienteId(clienteId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private CuentaDTO convertToDTO(Cuenta cuenta) {
        CuentaDTO dto = new CuentaDTO();
        dto.setId(cuenta.getId());
        dto.setNumeroCuenta(cuenta.getNumeroCuenta());
        dto.setTipoCuenta(cuenta.getTipoCuenta());
        dto.setSaldoInicial(cuenta.getSaldoInicial());
        dto.setEstado(cuenta.getEstado());
        dto.setClienteId(cuenta.getClienteId());
        return dto;
    }

    private Cuenta convertToEntity(CuentaDTO dto) {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(dto.getNumeroCuenta());
        cuenta.setTipoCuenta(dto.getTipoCuenta());
        cuenta.setSaldoInicial(dto.getSaldoInicial());
        cuenta.setEstado(dto.getEstado());
        cuenta.setClienteId(dto.getClienteId());
        return cuenta;
    }

    private void updateEntityFromDTO(Cuenta cuenta, CuentaDTO dto) {
        cuenta.setNumeroCuenta(dto.getNumeroCuenta());
        cuenta.setTipoCuenta(dto.getTipoCuenta());
        cuenta.setSaldoInicial(dto.getSaldoInicial());
        cuenta.setEstado(dto.getEstado());
        cuenta.setClienteId(dto.getClienteId());
    }
}