-- DDL PostgreSQL - Modelo de datos Franquicias
-- Entidades: Franquicia (1) -> Sucursal (N) -> Producto (N)
--
-- Nota de diseño (ver docs/decisiones/0001-identificador-unico-de-negocio.md):
-- "nombre" NO es clave de unicidad en ningún nivel (un espacio o un caracter distinto
-- genera duplicados accidentales). La unicidad real la dan atributos de negocio dedicados.
--
-- Nota de diseño (ver docs/decisiones/0002-borrado-logico-de-producto.md):
-- "producto" usa borrado lógico (columna "activo"); el código de un producto eliminado
-- NO se libera (uq_producto_sucursal_codigo se mantiene sin filtrar por "activo").

CREATE TABLE IF NOT EXISTS franquicia (
    id                BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre            VARCHAR(120) NOT NULL,
    tipo_documento    VARCHAR(10) NOT NULL,
    numero_documento  VARCHAR(30) NOT NULL,
    CONSTRAINT chk_franquicia_tipo_documento
        CHECK (tipo_documento IN ('NIT', 'CC', 'CE')),
    CONSTRAINT uq_franquicia_documento
        UNIQUE (tipo_documento, numero_documento)
);

CREATE TABLE IF NOT EXISTS sucursal (
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre        VARCHAR(120) NOT NULL,
    codigo        VARCHAR(30) NOT NULL,
    franquicia_id BIGINT NOT NULL,
    CONSTRAINT fk_sucursal_franquicia
        FOREIGN KEY (franquicia_id) REFERENCES franquicia (id)
        ON DELETE CASCADE,
    CONSTRAINT uq_sucursal_franquicia_codigo
        UNIQUE (franquicia_id, codigo)
);

CREATE TABLE IF NOT EXISTS producto (
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre      VARCHAR(120) NOT NULL,
    codigo      VARCHAR(30) NOT NULL,
    stock       INTEGER NOT NULL DEFAULT 0,
    activo      BOOLEAN NOT NULL DEFAULT true,
    sucursal_id BIGINT NOT NULL,
    CONSTRAINT fk_producto_sucursal
        FOREIGN KEY (sucursal_id) REFERENCES sucursal (id)
        ON DELETE CASCADE,
    CONSTRAINT uq_producto_sucursal_codigo
        UNIQUE (sucursal_id, codigo),
    CONSTRAINT chk_producto_stock_no_negativo
        CHECK (stock >= 0)
);

-- Índice compuesto para soportar "producto con más stock por sucursal"
CREATE INDEX idx_producto_sucursal_stock ON producto (sucursal_id, stock DESC);
