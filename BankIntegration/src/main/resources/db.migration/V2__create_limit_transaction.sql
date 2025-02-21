CREATE TABLE limit_transaction (
    id BIGSERIAL PRIMARY KEY,
    account_from BIGINT NOT NULL,
    limit_sum DECIMAL(19, 2) NOT NULL,
    remaining_limit DECIMAL(19, 2),
    limit_date_time TIMESTAMP WITH TIME ZONE,
    expense_category VARCHAR(255),
    limit_currency VARCHAR(3) NOT NULL
);