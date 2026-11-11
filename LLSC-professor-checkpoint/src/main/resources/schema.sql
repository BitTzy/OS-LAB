CREATE TABLE IF NOT EXISTS item_request (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    item VARCHAR(100) NOT NULL,
    lender VARCHAR(80) NOT NULL,
    borrower VARCHAR(80) NOT NULL,
    start_date DATE NOT NULL,
    due_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING_TERMS',
    CHECK (due_date >= start_date),
    CHECK (LOWER(lender) <> LOWER(borrower)),
    CHECK (status = 'PENDING_TERMS')
);
