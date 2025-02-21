CREATE TABLE exchange_rate (
    id BIGSERIAL PRIMARY KEY,
    symbol VARCHAR(255),
    rate DECIMAL(19, 4),
    date TIMESTAMP WITH TIME ZONE
);