package com.bank.cuenta.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cliente-service", url = "${cliente.service.url:http://localhost:8081}")
public interface ClienteClient {
    
    @GetMapping("/clientes/cliente-id/{clienteId}")
    ClienteDTO getClienteByClienteId(@PathVariable("clienteId") String clienteId);
    
    public static class ClienteDTO {
        private Long id;
        private String nombre;
        private String genero;
        private Integer edad;
        private String identificacion;
        private String direccion;
        private String telefono;
        private String clienteId;
        private String contrasena;
        private Boolean estado;
        
        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        
        public String getGenero() { return genero; }
        public void setGenero(String genero) { this.genero = genero; }
        
        public Integer getEdad() { return edad; }
        public void setEdad(Integer edad) { this.edad = edad; }
        
        public String getIdentificacion() { return identificacion; }
        public void setIdentificacion(String identificacion) { this.identificacion = identificacion; }
        
        public String getDireccion() { return direccion; }
        public void setDireccion(String direccion) { this.direccion = direccion; }
        
        public String getTelefono() { return telefono; }
        public void setTelefono(String telefono) { this.telefono = telefono; }
        
        public String getClienteId() { return clienteId; }
        public void setClienteId(String clienteId) { this.clienteId = clienteId; }
        
        public String getContrasena() { return contrasena; }
        public void setContrasena(String contrasena) { this.contrasena = contrasena; }
        
        public Boolean getEstado() { return estado; }
        public void setEstado(Boolean estado) { this.estado = estado; }
    }
}