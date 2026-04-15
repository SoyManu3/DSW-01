ALTER TABLE empleados
    ADD COLUMN IF NOT EXISTS departamento_id BIGINT;

UPDATE empleados
SET departamento_id = (SELECT id FROM departamentos WHERE nombre = 'General' ORDER BY id ASC LIMIT 1)
WHERE departamento_id IS NULL;

ALTER TABLE empleados
    ALTER COLUMN departamento_id SET NOT NULL;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.table_constraints
        WHERE constraint_name = 'fk_empleados_departamento'
          AND table_name = 'empleados'
    ) THEN
        ALTER TABLE empleados
            ADD CONSTRAINT fk_empleados_departamento
            FOREIGN KEY (departamento_id)
            REFERENCES departamentos(id);
    END IF;
END $$;

CREATE INDEX IF NOT EXISTS idx_empleados_departamento_id ON empleados (departamento_id);
