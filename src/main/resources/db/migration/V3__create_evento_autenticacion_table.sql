CREATE TABLE IF NOT EXISTS evento_autenticacion (
    id BIGSERIAL PRIMARY KEY,
    correo VARCHAR(320) NOT NULL,
    empleado_clave VARCHAR(50),
    resultado VARCHAR(40) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    origen VARCHAR(255)
);

CREATE INDEX IF NOT EXISTS idx_evento_auth_correo ON evento_autenticacion (correo);
CREATE INDEX IF NOT EXISTS idx_evento_auth_created_at ON evento_autenticacion (created_at);
