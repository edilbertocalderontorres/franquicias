-- DDL PostgreSQL - Modelo de datos Franquicias
-- Entidades: Franquicia (1) -> Sucursal (N) -> Producto (N)

CREATE TABLE IF NOT EXISTS franquicia (
    id     BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre VARCHAR(120) NOT NULL,
    CONSTRAINT uq_franquicia_nombre UNIQUE (nombre)
);

CREATE TABLE IF NOT EXISTS sucursal (
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre        VARCHAR(120) NOT NULL,
    franquicia_id BIGINT NOT NULL,
    CONSTRAINT fk_sucursal_franquicia
        FOREIGN KEY (franquicia_id) REFERENCES franquicia (id)
        ON DELETE CASCADE,
    CONSTRAINT uq_sucursal_nombre_por_franquicia
        UNIQUE (franquicia_id, nombre)
);

CREATE TABLE IF NOT EXISTS producto (
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre      VARCHAR(120) NOT NULL,
    stock       INTEGER NOT NULL DEFAULT 0,
    sucursal_id BIGINT NOT NULL,
    CONSTRAINT fk_producto_sucursal
        FOREIGN KEY (sucursal_id) REFERENCES sucursal (id)
        ON DELETE CASCADE,
    CONSTRAINT uq_producto_nombre_por_sucursal
        UNIQUE (sucursal_id, nombre),
    CONSTRAINT chk_producto_stock_no_negativo
        CHECK (stock >= 0)
);

-- Índice compuesto para soportar "producto con más stock por sucursal"
CREATE INDEX idx_producto_sucursal_stock ON producto (sucursal_id, stock DESC);
