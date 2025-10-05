DROP TABLE  IF EXISTS users;

create table if not exists users(
    id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(36) UNIQUE,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    username VARCHAR(50),
    email VARCHAR(50),
    role VARCHAR(15),
    number VARCHAR(50),
    account_creation_Date DATE
);