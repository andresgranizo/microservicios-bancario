package com.bank.cuenta.repository;

import com.bank.cuenta.entity.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    
    Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);
    
    List<Cuenta> findByClienteId(String clienteId);
    
    List<Cuenta> findByEstado(Boolean estado);
    
    List<Cuenta> findByClienteIdAndEstado(String clienteId, Boolean estado);
    
    boolean existsByNumeroCuenta(String numeroCuenta);
    
    @Query("SELECT c FROM Cuenta c WHERE c.clienteId = :clienteId AND c.estado = true")
    List<Cuenta> findCuentasActivasByClienteId(@Param("clienteId") String clienteId);
}