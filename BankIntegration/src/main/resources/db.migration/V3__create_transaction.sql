CREATE TABLE transaction (
    id BIGSERIAL PRIMARY KEY,
    account_from BIGINT NOT NULL,
    account_to BIGINT NOT NULL,
    sum DECIMAL(19, 2) NOT NULL,
    currency_short_name VARCHAR(3) NOT NULL,
    date_time TIMESTAMP WITH TIME ZONE,
    rate DOUBLE PRECISION,
    expense_category VARCHAR(255),
    limit_exceeded BOOLEAN,
    limit_id BIGINT REFERENCES limit_transaction(id)
);