ALTER TABLE empleados
    ADD COLUMN correo VARCHAR(320),
    ADD COLUMN password_hash VARCHAR(255),
    ADD COLUMN activo BOOLEAN NOT NULL DEFAULT TRUE,
    ADD COLUMN intentos_fallidos INTEGER NOT NULL DEFAULT 0,
    ADD COLUMN bloqueado_hasta TIMESTAMP NULL;

UPDATE empleados
SET correo = concat('empleado', numero_autonumerico, '@local.test'),
    password_hash = '$2a$10$4D7f0A2u3m4N6VtujQ/Ts.38JYQ0z8iNQWm9ThM8HS7MxL6P2Pv1e'
WHERE correo IS NULL
   OR password_hash IS NULL;

ALTER TABLE empleados
    ALTER COLUMN correo SET NOT NULL,
    ALTER COLUMN password_hash SET NOT NULL;

CREATE UNIQUE INDEX ux_empleados_correo ON empleados (lower(correo));

CREATE TABLE evento_autenticacion (
    id BIGSERIAL PRIMARY KEY,
    correo VARCHAR(320) NOT NULL,
    empleado_clave VARCHAR(50),
    resultado VARCHAR(40) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    origen VARCHAR(255)
);

CREATE INDEX idx_evento_auth_correo ON evento_autenticacion (correo);
CREATE INDEX idx_evento_auth_created_at ON evento_autenticacion (created_at);
