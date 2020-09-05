DROP TABLE IF EXISTS chave_documento;

CREATE TABLE chave_documento (
    id          INT         AUTO_INCREMENT  PRIMARY KEY,
    id_empresa  INT(6)      NOT NULL,
    valor_chave INT(44)     NOT NULL,
    status      VARCHAR(10) NOT NULL
);
