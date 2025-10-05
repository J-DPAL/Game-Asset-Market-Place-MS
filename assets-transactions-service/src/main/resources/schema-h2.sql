create table if not exists asset_transactions(
    id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
    asset_transaction_id VARCHAR(36) UNIQUE,
    user_id VARCHAR(36),
    payment_id VARCHAR(36),
    asset_id VARCHAR(36),
    status VARCHAR(15),
    type VARCHAR(15),
    percentage DECIMAL(19,2),
    amount_off DECIMAL(19,2),
    reason VARCHAR(50)
);