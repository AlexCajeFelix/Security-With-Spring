CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    documento VARCHAR(20) NOT NULL,  
    tipo ENUM('USER', 'ADMIN') NOT NULL DEFAULT 'USER'
);
