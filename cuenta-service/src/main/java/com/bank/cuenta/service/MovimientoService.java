package com.bank.cuenta.service;

import com.bank.cuenta.dto.MovimientoDTO;
import com.bank.cuenta.entity.Cuenta;
import com.bank.cuenta.entity.Movimiento;
import com.bank.cuenta.exception.CuentaNotFoundException;
import com.bank.cuenta.exception.SaldoInsuficienteException;
import com.bank.cuenta.repository.CuentaRepository;
import com.bank.cuenta.repository.MovimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MovimientoService {

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private CuentaRepository cuentaRepository;

    public List<MovimientoDTO> findAll() {
        return movimientoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public MovimientoDTO findById(Long id) {
        Movimiento movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new CuentaNotFoundException("Movimiento no encontrado con ID: " + id));
        return convertToDTO(movimiento);
    }

    public MovimientoDTO create(MovimientoDTO movimientoDTO) {
        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(movimientoDTO.getNumeroCuenta())
                .orElseThrow(() -> new CuentaNotFoundException("Cuenta no encontrada: " + movimientoDTO.getNumeroCuenta()));

        // Calcular el saldo actual
        BigDecimal saldoActual = calcularSaldoActual(cuenta);

        BigDecimal nuevoSaldo = saldoActual.add(movimientoDTO.getValor());

        // Verificar si el saldo es suficiente para retiros
        if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new SaldoInsuficienteException("Saldo no disponible");
        }

        // Crear el movimiento
        Movimiento movimiento = new Movimiento();
        movimiento.setTipoMovimiento(movimientoDTO.getTipoMovimiento());
        movimiento.setValor(movimientoDTO.getValor());
        movimiento.setSaldo(nuevoSaldo);
        movimiento.setCuenta(cuenta);
        movimiento.setFecha(LocalDateTime.now());

        Movimiento savedMovimiento = movimientoRepository.save(movimiento);
        return convertToDTO(savedMovimiento);
    }

    public List<MovimientoDTO> findByNumeroCuenta(String numeroCuenta) {
        return movimientoRepository.findByCuentaNumeroCuenta(numeroCuenta).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<MovimientoDTO> findByClienteIdAndFechaBetween(String clienteId, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return movimientoRepository.findByClienteIdAndFechaBetween(clienteId, fechaInicio, fechaFin).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public MovimientoDTO update(Long id, MovimientoDTO movimientoDTO) {
        System.out.println("Iniciando update para movimiento ID: " + id);

        Movimiento existingMovimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new CuentaNotFoundException("Movimiento no encontrado con ID: " + id));

        System.out.println("Movimiento encontrado: " + existingMovimiento.getId());

        // Actualizar solo el tipo de movimiento y valor, manteniendo todo lo demás igual
        if (movimientoDTO.getTipoMovimiento() != null && !movimientoDTO.getTipoMovimiento().trim().isEmpty()) {
            existingMovimiento.setTipoMovimiento(movimientoDTO.getTipoMovimiento());
            System.out.println("Tipo de movimiento actualizado: " + movimientoDTO.getTipoMovimiento());
        }

        if (movimientoDTO.getValor() != null) {
            existingMovimiento.setValor(movimientoDTO.getValor());
            System.out.println("Valor actualizado: " + movimientoDTO.getValor());
        }

        System.out.println("Guardando movimiento actualizado...");
        Movimiento updatedMovimiento = movimientoRepository.save(existingMovimiento);
        System.out.println("Movimiento guardado exitosamente");

        return convertToDTO(updatedMovimiento);
    }

    public void delete(Long id) {
        if (!movimientoRepository.existsById(id)) {
            throw new CuentaNotFoundException("Movimiento no encontrado con ID: " + id);
        }
        movimientoRepository.deleteById(id);
    }

    private BigDecimal calcularSaldoActual(Cuenta cuenta) {
        List<Movimiento> movimientos = movimientoRepository.findByCuentaIdOrderByFechaDesc(cuenta.getId());

        if (movimientos.isEmpty()) {
            return cuenta.getSaldoInicial();
        }

        return movimientos.get(0).getSaldo(); // El saldo más reciente
    }

    private MovimientoDTO convertToDTO(Movimiento movimiento) {
        MovimientoDTO dto = new MovimientoDTO();
        dto.setId(movimiento.getId());
        dto.setFecha(movimiento.getFecha());
        dto.setTipoMovimiento(movimiento.getTipoMovimiento());
        dto.setValor(movimiento.getValor());
        dto.setSaldo(movimiento.getSaldo());
        dto.setNumeroCuenta(movimiento.getCuenta().getNumeroCuenta());
        return dto;
    }
}
