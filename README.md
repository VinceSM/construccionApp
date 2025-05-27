Mysql: 

CREATE DATABASE IF NOT EXISTS estilodb;
USE estilodb;

-- ============================
-- TABLA: cliente
-- ============================

CREATE TABLE cliente (
    
    idCliente INT AUTO_INCREMENT PRIMARY KEY,
    nombreCompleto VARCHAR(100) NOT NULL,
    dni VARCHAR(20) UNIQUE NOT NULL,
    telefono VARCHAR(20),
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
    deletedAt TIMESTAMP NULL
);

-- ============================
-- TABLA: estado_pedido
-- ============================

CREATE TABLE estado_pedido (

    idEstado INT AUTO_INCREMENT PRIMARY KEY,
    tipo VARCHAR(50) NOT NULL,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
    deletedAt TIMESTAMP NULL
);

-- ============================
-- TABLA: producto
-- ============================

CREATE TABLE producto (

    idProducto INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    precio DECIMAL(10, 2) NOT NULL,
    costo DECIMAL(10, 2) NOT NULL,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
    deletedAt TIMESTAMP NULL
);

ALTER TABLE `producto` ADD `descripcion` VARCHAR(65) NOT NULL AFTER `nombre`;

-- ============================
-- TABLA: stock
-- ============================

CREATE TABLE stock (

    idStock INT AUTO_INCREMENT PRIMARY KEY,
    idProducto INT NOT NULL,
    unidad DECIMAL(10, 2) NOT NULL,
    medida VARCHAR(20) NOT NULL, -- Ej: "kg", "m³", "unidad", etc.
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
    deletedAt TIMESTAMP NULL,
    FOREIGN KEY (idProducto) REFERENCES producto(idProducto)
);

-- ============================
-- TABLA: pedido
-- ============================

CREATE TABLE pedido (

    idPedido INT AUTO_INCREMENT PRIMARY KEY,
    idCliente INT NOT NULL,
    idEstado INT NOT NULL,
    fechaPedido TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
    deletedAt TIMESTAMP NULL,
    FOREIGN KEY (idCliente) REFERENCES cliente(idCliente),
    FOREIGN KEY (idEstado) REFERENCES estado_pedido(idEstado)
);

-- ============================
-- TABLA: item
-- ============================

CREATE TABLE item (

    idItem int not null PRIMARY KEY,
    idProducto int not null,
    cantidad int not null,
    FOREIGN KEY (idProducto) REFERENCES producto(idProducto)
);

INSERT INTO estado_pedido (tipo, createdAt) VALUES 
  ('Pendiente', NOW()),
  ('Confirmado', NOW()),
  ('En preparación', NOW()),
  ('En camino', NOW()),
  ('Entregado', NOW()),
  ('Cancelado', NOW());

-- Tabla producto
CREATE INDEX idx_producto_nombre ON producto(nombre);
CREATE INDEX idx_producto_deletedAt ON producto(deletedAt);

-- Tabla precio_producto
CREATE INDEX idx_precio_producto_idProducto ON precio_producto(idProducto);
CREATE INDEX idx_precio_producto_createdAt ON precio_producto(createdAt);
CREATE INDEX idx_precio_producto_deletedAt ON precio_producto(deletedAt);

-- Tabla stock
CREATE INDEX idx_stock_idProducto ON stock(idProducto);
CREATE INDEX idx_stock_deletedAt ON stock(deletedAt);

-- Tabla estado_pedido
CREATE INDEX idx_estado_pedido_tipo ON estado_pedido(tipo);
CREATE INDEX idx_estado_pedido_deletedAt ON estado_pedido(deletedAt);

-- Tabla cliente
CREATE INDEX idx_cliente_dni ON cliente(dni);
CREATE INDEX idx_cliente_nombreCompleto ON cliente(nombreCompleto);

-- Tabla pedido
CREATE INDEX idx_pedido_idCliente ON pedido(idCliente);
CREATE INDEX idx_pedido_idEstado ON pedido(idEstado);
CREATE INDEX idx_pedido_fechaPedido ON pedido(fechaPedido);

-- Tabla detalle_pedido
CREATE INDEX idx_detalle_idPedido ON detalle_pedido(idPedido);
CREATE INDEX idx_detalle_idProducto ON detalle_pedido(idProducto);
