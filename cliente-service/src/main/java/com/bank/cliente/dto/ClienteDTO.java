package com.bank.cliente.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ClienteDTO {
    
    private Long id;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String nombre;
    
    @NotBlank(message = "El género es obligatorio")
    @Size(max = 10)
    private String genero;
    
    @NotNull(message = "La edad es obligatoria")
    private Integer edad;
    
    @NotBlank(message = "La identificación es obligatoria")
    @Size(max = 20)
    private String identificacion;
    
    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 200)
    private String direccion;
    
    @NotBlank(message = "El teléfono es obligatorio")
    @Size(max = 15)
    private String telefono;
    
    @NotBlank(message = "El ID del cliente es obligatorio")
    @Size(max = 20)
    private String clienteId;
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 4, max = 100)
    private String contrasena;
    
    @NotNull(message = "El estado es obligatorio")
    private Boolean estado;
    
    public ClienteDTO() {}
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getGenero() {
        return genero;
    }
    
    public void setGenero(String genero) {
        this.genero = genero;
    }
    
    public Integer getEdad() {
        return edad;
    }
    
    public void setEdad(Integer edad) {
        this.edad = edad;
    }
    
    public String getIdentificacion() {
        return identificacion;
    }
    
    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }
    
    public String getDireccion() {
        return direccion;
    }
    
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public String getClienteId() {
        return clienteId;
    }
    
    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }
    
    public String getContrasena() {
        return contrasena;
    }
    
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    
    public Boolean getEstado() {
        return estado;
    }
    
    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}