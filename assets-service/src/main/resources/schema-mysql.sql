DROP TABLE  IF EXISTS assets;

create table if not exists assets(
    id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
    asset_id VARCHAR(36) UNIQUE,
    name VARCHAR(50),
    description VARCHAR(100),
    asset_type VARCHAR(50),
    price DECIMAL(19,2),
    file_url VARCHAR(100),
    thumbnail_url VARCHAR(100),
    license_type VARCHAR(20),
    created_date DATE,
    updated_date DATE
);