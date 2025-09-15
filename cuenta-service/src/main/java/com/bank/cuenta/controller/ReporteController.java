package com.bank.cuenta.controller;

import com.bank.cuenta.dto.EstadoCuentaDTO;
import com.bank.cuenta.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reportes")
@CrossOrigin(origins = "*")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    // F4: Endpoint para generar reporte de estado de cuenta
    // Formato: /reportes?fecha=2022-02-01,2022-02-28&cliente=clienteId
    @GetMapping
    public ResponseEntity<List<EstadoCuentaDTO>> generarReporte(
            @RequestParam("fecha") String rangoFechas,
            @RequestParam("cliente") String clienteId) {
        
        String[] fechas = rangoFechas.split(",");
        if (fechas.length != 2) {
            throw new IllegalArgumentException("El formato de fecha debe ser: fecha=2022-02-01,2022-02-28");
        }
        
        String fechaInicio = fechas[0].trim();
        String fechaFin = fechas[1].trim();
        
        List<EstadoCuentaDTO> reporte = reporteService.generarEstadoCuenta(clienteId, fechaInicio, fechaFin);
        return ResponseEntity.ok(reporte);
    }
}