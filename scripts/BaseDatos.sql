-- Script de Base de Datos para Sistema Bancario

-- Crear esquemas si no existen
CREATE SCHEMA IF NOT EXISTS cliente_service;
CREATE SCHEMA IF NOT EXISTS cuenta_service;

-- ESQUEMA: cliente_service

-- Tabla Personas
CREATE TABLE IF NOT EXISTS cliente_service.personas (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    genero VARCHAR(10) NOT NULL,
    edad INTEGER NOT NULL,
    identificacion VARCHAR(20) NOT NULL UNIQUE,
    direccion VARCHAR(200) NOT NULL,
    telefono VARCHAR(15) NOT NULL,
    CONSTRAINT chk_edad CHECK (edad > 0)
);

-- Tabla Clientes (hereda de Personas)
CREATE TABLE IF NOT EXISTS cliente_service.clientes (
    persona_id BIGINT PRIMARY KEY REFERENCES cliente_service.personas(id) ON DELETE CASCADE,
    cliente_id VARCHAR(20) NOT NULL UNIQUE,
    contrasena VARCHAR(100) NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT true
);


-- ESQUEMA: cuenta_service


-- Tabla Cuentas
CREATE TABLE IF NOT EXISTS cuenta_service.cuentas (
    id BIGSERIAL PRIMARY KEY,
    numero_cuenta VARCHAR(20) NOT NULL UNIQUE,
    tipo_cuenta VARCHAR(20) NOT NULL,
    saldo_inicial DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    estado BOOLEAN NOT NULL DEFAULT true,
    cliente_id VARCHAR(20) NOT NULL,
    CONSTRAINT chk_saldo_inicial CHECK (saldo_inicial >= 0),
    CONSTRAINT chk_tipo_cuenta CHECK (tipo_cuenta IN ('Ahorro', 'Corriente'))
);

-- Tabla Movimientos
CREATE TABLE IF NOT EXISTS cuenta_service.movimientos (
    id BIGSERIAL PRIMARY KEY,
    fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tipo_movimiento VARCHAR(20) NOT NULL,
    valor DECIMAL(15,2) NOT NULL,
    saldo DECIMAL(15,2) NOT NULL,
    cuenta_id BIGINT NOT NULL REFERENCES cuenta_service.cuentas(id) ON DELETE CASCADE,
    CONSTRAINT chk_tipo_movimiento CHECK (tipo_movimiento IN ('Deposito', 'Retiro'))
);


-- ÍNDICES PARA OPTIMIZACIÓN


-- Índices para cliente_service
CREATE INDEX IF NOT EXISTS idx_personas_identificacion ON cliente_service.personas(identificacion);
CREATE INDEX IF NOT EXISTS idx_clientes_cliente_id ON cliente_service.clientes(cliente_id);
CREATE INDEX IF NOT EXISTS idx_clientes_estado ON cliente_service.clientes(estado);

-- Índices para cuenta_service
CREATE INDEX IF NOT EXISTS idx_cuentas_numero_cuenta ON cuenta_service.cuentas(numero_cuenta);
CREATE INDEX IF NOT EXISTS idx_cuentas_cliente_id ON cuenta_service.cuentas(cliente_id);
CREATE INDEX IF NOT EXISTS idx_cuentas_estado ON cuenta_service.cuentas(estado);
CREATE INDEX IF NOT EXISTS idx_movimientos_cuenta_id ON cuenta_service.movimientos(cuenta_id);
CREATE INDEX IF NOT EXISTS idx_movimientos_fecha ON cuenta_service.movimientos(fecha);


-- DATOS DE EJEMPLO (CASOS DE USO)


-- Insertar personas
INSERT INTO cliente_service.personas (nombre, genero, edad, identificacion, direccion, telefono)
VALUES
    ('Jose Lema', 'Masculino', 30, '1234567890', 'Otavalo sn y principal', '098254785'),
    ('Marianela Montalvo', 'Femenino', 28, '0987654321', 'Amazonas y NNUU', '097548965'),
    ('Juan Osorio', 'Masculino', 35, '1122334455', '13 junio y Equinoccial', '098874587')
ON CONFLICT (identificacion) DO NOTHING;

-- Insertar clientes
INSERT INTO cliente_service.clientes (persona_id, cliente_id, contrasena, estado)
SELECT p.id, 'JOSE001', '1234', true FROM cliente_service.personas p WHERE p.identificacion = '1234567890'
UNION ALL
SELECT p.id, 'MARIA001', '5678', true FROM cliente_service.personas p WHERE p.identificacion = '0987654321'
UNION ALL
SELECT p.id, 'JUAN001', '1245', true FROM cliente_service.personas p WHERE p.identificacion = '1122334455'
ON CONFLICT (cliente_id) DO NOTHING;

-- Insertar cuentas
INSERT INTO cuenta_service.cuentas (numero_cuenta, tipo_cuenta, saldo_inicial, estado, cliente_id)
VALUES
    ('478758', 'Ahorro', 2000.00, true, 'JOSE001'),
    ('225487', 'Corriente', 100.00, true, 'MARIA001'),
    ('495878', 'Ahorro', 0.00, true, 'JUAN001'),
    ('496825', 'Ahorro', 540.00, true, 'MARIA001'),
    ('585545', 'Corriente', 1000.00, true, 'JOSE001')
ON CONFLICT (numero_cuenta) DO NOTHING;

-- Insertar movimientos de ejemplo
INSERT INTO cuenta_service.movimientos (fecha, tipo_movimiento, valor, saldo, cuenta_id)
SELECT
    '2022-02-10 10:00:00'::timestamp,
    'Deposito',
    600.00,
    700.00,
    c.id
FROM cuenta_service.cuentas c
WHERE c.numero_cuenta = '225487'
UNION ALL
SELECT
    '2022-02-08 15:30:00'::timestamp,
    'Retiro',
    -540.00,
    0.00,
    c.id
FROM cuenta_service.cuentas c
WHERE c.numero_cuenta = '496825'
UNION ALL
SELECT
    '2022-02-05 09:15:00'::timestamp,
    'Retiro',
    -575.00,
    1425.00,
    c.id
FROM cuenta_service.cuentas c
WHERE c.numero_cuenta = '478758'
UNION ALL
SELECT
    '2022-02-03 14:20:00'::timestamp,
    'Deposito',
    150.00,
    150.00,
    c.id
FROM cuenta_service.cuentas c
WHERE c.numero_cuenta = '495878';


-- COMENTARIOS Y DOCUMENTACIÓN


COMMENT ON SCHEMA cliente_service IS 'Esquema para el microservicio de gestión de clientes y personas';
COMMENT ON SCHEMA cuenta_service IS 'Esquema para el microservicio de gestión de cuentas y movimientos';

COMMENT ON TABLE cliente_service.personas IS 'Tabla base para almacenar información personal';
COMMENT ON TABLE cliente_service.clientes IS 'Tabla que extiende personas con información específica de cliente';
COMMENT ON TABLE cuenta_service.cuentas IS 'Tabla para almacenar información de cuentas bancarias';
COMMENT ON TABLE cuenta_service.movimientos IS 'Tabla para registrar todas las transacciones de las cuentas';

COMMENT ON COLUMN cuenta_service.movimientos.valor IS 'Valor del movimiento: positivo para depósitos, negativo para retiros';
COMMENT ON COLUMN cuenta_service.movimientos.saldo IS 'Saldo de la cuenta después del movimiento';


-- VERIFICACIONES FINALES


-- Mostrar estadísticas
SELECT
    'Personas creadas' as descripcion,
    count(*) as total
FROM cliente_service.personas
UNION ALL
SELECT
    'Clientes creados' as descripcion,
    count(*) as total
FROM cliente_service.clientes
UNION ALL
SELECT
    'Cuentas creadas' as descripcion,
    count(*) as total
FROM cuenta_service.cuentas
UNION ALL
SELECT
    'Movimientos creados' as descripcion,
    count(*) as total
FROM cuenta_service.movimientos;
