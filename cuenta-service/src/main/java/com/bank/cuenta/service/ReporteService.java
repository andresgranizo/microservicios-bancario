package com.bank.cuenta.service;

import com.bank.cuenta.client.ClienteClient;
import com.bank.cuenta.dto.EstadoCuentaDTO;
import com.bank.cuenta.entity.Movimiento;
import com.bank.cuenta.repository.MovimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReporteService {

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private ClienteClient clienteClient;

    // F4: Generar reporte de estado de cuenta por rango de fechas y cliente
    public List<EstadoCuentaDTO> generarEstadoCuenta(String clienteId, String fechaInicio, String fechaFin) {
        LocalDateTime inicio = LocalDateTime.parse(fechaInicio + "T00:00:00");
        LocalDateTime fin = LocalDateTime.parse(fechaFin + "T23:59:59");

        // Obtener información del cliente desde el microservicio de clientes
        ClienteClient.ClienteDTO cliente;
        try {
            cliente = clienteClient.getClienteByClienteId(clienteId);
        } catch (Exception e) {
            throw new RuntimeException("Cliente no encontrado: " + clienteId);
        }

        // Obtener movimientos del cliente en el rango de fechas
        List<Movimiento> movimientos = movimientoRepository.findByClienteIdAndFechaBetween(clienteId, inicio, fin);

        return movimientos.stream()
                .map(movimiento -> new EstadoCuentaDTO(
                        movimiento.getFecha(),
                        cliente.getNombre(),
                        movimiento.getCuenta().getNumeroCuenta(),
                        movimiento.getCuenta().getTipoCuenta(),
                        movimiento.getCuenta().getSaldoInicial(),
                        movimiento.getCuenta().getEstado(),
                        movimiento.getValor(),
                        movimiento.getSaldo()
                ))
                .collect(Collectors.toList());
    }
}