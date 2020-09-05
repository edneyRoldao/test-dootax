DROP TABLE IF EXISTS document_key;

CREATE TABLE document_key (
    id          INT AUTO_INCREMENT  PRIMARY KEY,
    company_id  INT(6)      NOT NULL,
    key_value   INT(44)     NOT NULL,
    status      VARCHAR(10) NOT NULL
);
