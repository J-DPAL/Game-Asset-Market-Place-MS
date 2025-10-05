DROP TABLE  IF EXISTS payments;

create table if not exists payments(
    id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
    payment_id VARCHAR(36) UNIQUE,
    price DECIMAL(19,2),
    currency VARCHAR(10),
    payment_type VARCHAR(15),
    transaction_status VARCHAR(15)
);