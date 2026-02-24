DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'employee_type') THEN
        CREATE TYPE employee_type AS ENUM ('INDIVIDUAL', 'LEGAL_ENTITY');
    END IF;
END$$;

CREATE TABLE IF NOT EXISTS employees (
    id BIGSERIAL PRIMARY KEY,
    type employee_type NOT NULL,
    cpf VARCHAR(11),
    cnpj VARCHAR(14),
    rg VARCHAR(20),
    birth_date DATE,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    cep VARCHAR(10) NOT NULL,
    state VARCHAR(2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_document CHECK (
        (type = 'INDIVIDUAL' AND cpf IS NOT NULL AND cnpj IS NULL)
        OR
        (type = 'LEGAL_ENTITY' AND cnpj IS NOT NULL AND cpf IS NULL)
    )
);

CREATE UNIQUE INDEX IF NOT EXISTS unique_cpf
    ON employees(cpf)
    WHERE cpf IS NOT NULL;

CREATE UNIQUE INDEX IF NOT EXISTS unique_employee_cnpj
    ON employees(cnpj)
    WHERE cnpj IS NOT NULL;