# Microservicios Bancario

[![CI/CD Pipeline](https://github.com/andresgranizo/microservicios-bancario/actions/workflows/ci.yml/badge.svg)](https://github.com/andresgranizo/microservicios-bancario/actions/workflows/ci.yml)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-enabled-blue.svg)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

Sistema bancario implementado con arquitectura de microservicios.

## Arquitectura

- **2 Microservicios:**
  - `cliente-service` (Puerto 8081): Gestión de clientes y personas
  - `cuenta-service` (Puerto 8082): Gestión de cuentas y movimientos
- **Comunicación asíncrona** entre microservicios usando Feign Client
- **Base de datos:** PostgreSQL
- **Mensajería:** RabbitMQ
- **Contenerización:** Docker

## Funcionalidades Implementadas

### F1: CRUD Endpoints
- `/clientes` - Gestión completa de clientes
- `/cuentas` - Gestión completa de cuentas
- `/movimientos` - Gestión de movimientos

### F2: Registro de Movimientos
- Valores positivos (depósitos) y negativos (retiros)
- Actualización automática del saldo disponible
- Registro completo de transacciones

### F3: Validación de Saldo
- Mensaje "Saldo no disponible" para movimientos sin saldo suficiente
- Validación automática antes de procesar retiros

###  F4: Reportes de Estado de Cuenta
- Endpoint: `/reportes?fecha=inicio,fin&cliente=clienteId`
- Reporte por rango de fechas y cliente específico
- Formato JSON con detalles completos

### F5: Pruebas Unitarias
- Tests para entidad Cliente
- Tests para servicio de Movimientos
- Cobertura de casos exitosos y de error

### F6: Pruebas de Integración
- Tests end-to-end para controladores
- Validación de API completa

### F7: Despliegue Docker
- Dockerfiles optimizados
- Docker Compose con PostgreSQL y RabbitMQ

##  Documentación de API (Swagger)

Una vez que los servicios estén ejecutándose, puedes acceder a la documentación interactiva de las APIs:

### **URLs de Swagger:**
- **Cliente Service**: http://localhost:8081/swagger-ui/index.html
- **Cuenta Service**: http://localhost:8082/swagger-ui/index.html

### **OpenAPI JSON:**
- **Cliente Service**: http://localhost:8081/v3/api-docs
- **Cuenta Service**: http://localhost:8082/v3/api-docs

Con Swagger puedes:
- Ver todos los endpoints disponibles
- Probar las APIs directamente desde la interfaz
- Ver los esquemas de datos (DTOs)
- Ejecutar requests con diferentes parámetros
- Ver ejemplos de request/response

##  RabbitMQ Management

### **Acceso a RabbitMQ:**
- **URL**: http://localhost:15672
- **Usuario**: `guest`
- **Contraseña**: `guest`

Desde la interfaz de RabbitMQ puedes:
- Monitorear colas de mensajes
- Ver estadísticas de tráfico
- Gestionar intercambios (exchanges)
- Supervisar conexiones entre servicios

##  Ejecución  con Makefile

```bash
# Comandos principales
make start    # Iniciar todos los servicios
make stop     # Detener servicios
make build    # Construir proyecto
make test     # Ejecutar pruebas
make clean    # Limpiar contenedores
make logs     # Ver logs

### Ejecución Paso a Paso

1. **Clonar el repositorio:**
```bash
git clone git@github.com:andresgranizo/microservicios-bancario.git
cd microservicios-bancario
```

2. **Iniciar servicios (RECOMENDADO):**
```bash
make start
```

3. **Verificar que esté funcionando:**
```bash
make logs
```

4. **Acceder a los servicios:**
   - **APIs Cliente**: http://localhost:8081/clientes
   - **APIs Cuenta**: http://localhost:8082/cuentas
   - **Swagger Cliente**: http://localhost:8081/swagger-ui/index.html
   - **Swagger Cuenta**: http://localhost:8082/swagger-ui/index.html
   - **RabbitMQ**: http://localhost:15672 (guest/guest)

### Alternativas de Ejecución

#### Desarrollo Local con Maven
```bash
# Cliente Service
cd cliente-service && mvn spring-boot:run

# Cuenta Service (nueva terminal)
cd cuenta-service && mvn spring-boot:run
```

#### Docker Compose Manual
```bash
docker-compose up -d      # Iniciar
docker-compose ps         # Verificar
docker-compose logs -f    # Ver logs
```

## Testing

### Ejecutar Pruebas Localmente
```bash
# Pruebas unitarias y de integración
make test

# O manualmente por servicio
cd cliente-service && mvn test
cd cuenta-service && mvn test
```

### CI/CD con GitHub Actions
El proyecto incluye un pipeline automatizado que:
- ✅ Ejecuta todas las pruebas automáticamente en cada push/PR
- ✅ Construye las imágenes Docker
- ✅ Valida la integridad del código
- ✅ Genera reportes de pruebas

**Estado actual:** ![CI/CD Status](https://github.com/andresgranizo/microservicios-bancario/actions/workflows/ci.yml/badge.svg)

### Postman Collection
Importar el archivo `Microservicios-Bancario.postman_collection.json` en Postman para probar todos los endpoints.

**Variables de entorno:**
- `cliente_service_url`: http://localhost:8081
- `cuenta_service_url`: http://localhost:8082

##  Base de Datos

El script `scripts/BaseDatos.sql` contiene:
- Esquemas para ambos microservicios
- Tablas con relaciones
- Datos de ejemplo según casos de uso

## Endpoints Principales

### Cliente Service (Puerto 8081)
- `GET /clientes` - Listar todos los clientes
- `POST /clientes` - Crear cliente
- `PUT /clientes/{id}` - Actualizar cliente
- `DELETE /clientes/{id}` - Eliminar cliente
- `GET /clientes/cliente-id/{clienteId}` - Buscar por ID de cliente

### Cuenta Service (Puerto 8082)
- `GET /cuentas` - Listar todas las cuentas
- `POST /cuentas` - Crear cuenta
- `PUT /cuentas/{id}` - Actualizar cuenta
- `DELETE /cuentas/{id}` - Eliminar cuenta
- `GET /cuentas/cliente/{clienteId}` - Cuentas por cliente

### Movimientos
- `POST /movimientos` - Registrar movimiento (con validaciones F2, F3)
- `GET /movimientos/cuenta/{numeroCuenta}` - Movimientos por cuenta

### Reportes
- `GET /reportes?fecha=inicio,fin&cliente=clienteId` - Estado de cuenta (F4)

##  Tecnologías Utilizadas

- **Backend:** Spring Boot 3.2.0, Java 17
- **Base de Datos:** PostgreSQL, H2 (desarrollo)
- **ORM:** Spring Data JPA, Hibernate
- **Testing:** JUnit 5, Mockito, Spring Boot Test
- **Containerización:** Docker, Docker Compose
- **Comunicación:** Spring Cloud OpenFeign
- **Mensajería:** RabbitMQ
- **Documentación:** SpringDoc OpenAPI 3 (Swagger UI)
- **CI/CD:** GitHub Actions


## Casos de Uso Implementados

1. **Creación de Usuarios**
2. **Creación de Cuentas**
3. **Movimientos Bancarios**
4. **Validación de Saldo**
5. **Reportes por Fechas**
