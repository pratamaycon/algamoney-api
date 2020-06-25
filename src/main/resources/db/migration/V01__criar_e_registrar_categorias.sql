
CREATE TABLE categoria (
codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
nome VARCHAR(50) NOT NULL
);

INSERT INTO categoria(nome) values("Lazer");
INSERT INTO categoria(nome) values("Alimentação");
INSERT INTO categoria(nome) values("SuperMercado");
INSERT INTO categoria(nome) values("Academia");
INSERT INTO categoria(nome) values("Fármacia");