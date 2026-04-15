ALTER TABLE empleados
    ADD COLUMN IF NOT EXISTS rol VARCHAR(20);

UPDATE empleados
SET rol = 'EMPLEADO'
WHERE rol IS NULL;

UPDATE empleados
SET rol = 'ADMIN'
WHERE lower(correo) IN ('admin', 'admin@local.test');

ALTER TABLE empleados
    ALTER COLUMN rol SET NOT NULL;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.constraint_column_usage
        WHERE table_name = 'empleados'
          AND constraint_name = 'chk_empleados_rol'
    ) THEN
        ALTER TABLE empleados
            ADD CONSTRAINT chk_empleados_rol CHECK (rol IN ('ADMIN', 'EMPLEADO'));
    END IF;
END $$;
