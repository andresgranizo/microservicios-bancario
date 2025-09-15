package com.bank.cliente.repository;

import com.bank.cliente.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    Optional<Cliente> findByClienteId(String clienteId);
    
    Optional<Cliente> findByIdentificacion(String identificacion);
    
    List<Cliente> findByEstado(Boolean estado);
    
    boolean existsByClienteId(String clienteId);
    
    boolean existsByIdentificacion(String identificacion);
}