package com.bank.cliente.repository;

import com.bank.cliente.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {
    
    Optional<Persona> findByIdentificacion(String identificacion);
    
    boolean existsByIdentificacion(String identificacion);
}