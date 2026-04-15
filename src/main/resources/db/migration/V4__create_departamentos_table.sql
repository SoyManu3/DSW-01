CREATE TABLE IF NOT EXISTS departamentos (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    CONSTRAINT uq_departamentos_nombre UNIQUE (nombre),
    CONSTRAINT chk_departamentos_nombre_len CHECK (char_length(trim(nombre)) BETWEEN 1 AND 100)
);

INSERT INTO departamentos (nombre)
VALUES ('General')
ON CONFLICT (nombre) DO NOTHING;
