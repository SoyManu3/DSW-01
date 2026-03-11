CREATE TABLE empleados (
    prefijo VARCHAR(3) NOT NULL,
    numero_autonumerico BIGINT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    direccion VARCHAR(100) NOT NULL,
    telefono VARCHAR(100) NOT NULL,
    CONSTRAINT pk_empleados PRIMARY KEY (prefijo, numero_autonumerico),
    CONSTRAINT chk_empleados_prefijo CHECK (prefijo = 'EMP'),
    CONSTRAINT chk_empleados_nombre_len CHECK (char_length(trim(nombre)) BETWEEN 1 AND 100),
    CONSTRAINT chk_empleados_direccion_len CHECK (char_length(trim(direccion)) BETWEEN 1 AND 100),
    CONSTRAINT chk_empleados_telefono_len CHECK (char_length(trim(telefono)) BETWEEN 1 AND 100)
);

CREATE INDEX idx_empleados_numero ON empleados (numero_autonumerico);
