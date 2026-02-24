CREATE TABLE IF NOT EXISTS company_employees (
    company_id BIGINT NOT NULL,
    employee_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (company_id, employee_id),

    CONSTRAINT fk_company
        FOREIGN KEY (company_id)
        REFERENCES companies(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_employee
        FOREIGN KEY (employee_id)
        REFERENCES employees(id)
        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_company_employees_company
    ON company_employees(company_id);

CREATE INDEX IF NOT EXISTS idx_company_employees_employee
    ON company_employees(employee_id);