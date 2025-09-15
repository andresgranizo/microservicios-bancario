package com.bank.cuenta.service;

import com.bank.cuenta.dto.MovimientoDTO;
import com.bank.cuenta.entity.Cuenta;
import com.bank.cuenta.entity.Movimiento;
import com.bank.cuenta.exception.CuentaNotFoundException;
import com.bank.cuenta.exception.SaldoInsuficienteException;
import com.bank.cuenta.repository.CuentaRepository;
import com.bank.cuenta.repository.MovimientoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovimientoServiceTest {

    @Mock
    private MovimientoRepository movimientoRepository;

    @Mock
    private CuentaRepository cuentaRepository;

    @InjectMocks
    private MovimientoService movimientoService;

    private Cuenta cuenta;
    private MovimientoDTO movimientoDTO;
    private Movimiento movimiento;

    @BeforeEach
    void setUp() {
        cuenta = new Cuenta();
        cuenta.setId(1L);
        cuenta.setNumeroCuenta("478758");
        cuenta.setTipoCuenta("Ahorro");
        cuenta.setSaldoInicial(new BigDecimal("2000.00"));
        cuenta.setEstado(true);
        cuenta.setClienteId("JOSE001");

        movimientoDTO = new MovimientoDTO();
        movimientoDTO.setTipoMovimiento("Deposito");
        movimientoDTO.setValor(new BigDecimal("500.00"));
        movimientoDTO.setNumeroCuenta("478758");

        movimiento = new Movimiento();
        movimiento.setId(1L);
        movimiento.setTipoMovimiento("Deposito");
        movimiento.setValor(new BigDecimal("500.00"));
        movimiento.setSaldo(new BigDecimal("2500.00"));
        movimiento.setCuenta(cuenta);
        movimiento.setFecha(LocalDateTime.now());
    }

    @Test
    void testCreateMovimientoDepositoSuccess() {
        when(cuentaRepository.findByNumeroCuenta("478758")).thenReturn(Optional.of(cuenta));
        when(movimientoRepository.findByCuentaIdOrderByFechaDesc(1L)).thenReturn(new ArrayList<>());
        when(movimientoRepository.save(any(Movimiento.class))).thenReturn(movimiento);

        MovimientoDTO result = movimientoService.create(movimientoDTO);

        assertNotNull(result);
        assertEquals("Deposito", result.getTipoMovimiento());
        assertEquals(new BigDecimal("500.00"), result.getValor());
        assertEquals("478758", result.getNumeroCuenta());
        verify(cuentaRepository, times(1)).findByNumeroCuenta("478758");
        verify(movimientoRepository, times(1)).save(any(Movimiento.class));
    }

    @Test
    void testCreateMovimientoRetiroSuccess() {
        movimientoDTO.setTipoMovimiento("Retiro");
        movimientoDTO.setValor(new BigDecimal("-575.00"));

        List<Movimiento> movimientosAnteriores = new ArrayList<>();
        Movimiento movimientoAnterior = new Movimiento();
        movimientoAnterior.setSaldo(new BigDecimal("2000.00"));
        movimientosAnteriores.add(movimientoAnterior);

        when(cuentaRepository.findByNumeroCuenta("478758")).thenReturn(Optional.of(cuenta));
        when(movimientoRepository.findByCuentaIdOrderByFechaDesc(1L)).thenReturn(movimientosAnteriores);
        when(movimientoRepository.save(any(Movimiento.class))).thenReturn(movimiento);

        MovimientoDTO result = movimientoService.create(movimientoDTO);

        assertNotNull(result);
        verify(cuentaRepository, times(1)).findByNumeroCuenta("478758");
        verify(movimientoRepository, times(1)).save(any(Movimiento.class));
    }

    @Test
    void testCreateMovimientoSaldoInsuficiente() {
        movimientoDTO.setTipoMovimiento("Retiro");
        movimientoDTO.setValor(new BigDecimal("-3000.00")); // Retiro mayor al saldo

        List<Movimiento> movimientosAnteriores = new ArrayList<>();
        Movimiento movimientoAnterior = new Movimiento();
        movimientoAnterior.setSaldo(new BigDecimal("2000.00"));
        movimientosAnteriores.add(movimientoAnterior);

        when(cuentaRepository.findByNumeroCuenta("478758")).thenReturn(Optional.of(cuenta));
        when(movimientoRepository.findByCuentaIdOrderByFechaDesc(1L)).thenReturn(movimientosAnteriores);

        assertThrows(SaldoInsuficienteException.class, () -> movimientoService.create(movimientoDTO));
        verify(cuentaRepository, times(1)).findByNumeroCuenta("478758");
        verify(movimientoRepository, never()).save(any(Movimiento.class));
    }

    @Test
    void testCreateMovimientoCuentaNotFound() {
        when(cuentaRepository.findByNumeroCuenta("478758")).thenReturn(Optional.empty());

        assertThrows(CuentaNotFoundException.class, () -> movimientoService.create(movimientoDTO));
        verify(cuentaRepository, times(1)).findByNumeroCuenta("478758");
        verify(movimientoRepository, never()).save(any(Movimiento.class));
    }

    @Test
    void testFindByIdSuccess() {
        when(movimientoRepository.findById(1L)).thenReturn(Optional.of(movimiento));

        MovimientoDTO result = movimientoService.findById(1L);

        assertNotNull(result);
        assertEquals("Deposito", result.getTipoMovimiento());
        assertEquals(new BigDecimal("500.00"), result.getValor());
        verify(movimientoRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(movimientoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CuentaNotFoundException.class, () -> movimientoService.findById(1L));
        verify(movimientoRepository, times(1)).findById(1L);
    }
}
