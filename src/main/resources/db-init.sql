CREATE TABLE IF NOT EXISTS users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  surname VARCHAR(255) NOT NULL,
  person_id VARCHAR(255) NOT NULL,
  uuid VARCHAR(255) NOT NULL,
  CONSTRAINT uq_personid UNIQUE (person_id),
  CONSTRAINT uq_uuid UNIQUE (uuid)
);

INSERT INTO users (name, surname, person_id, uuid)
VALUES ('Jack', 'Hero', '1', CONCAT('uuid-', FLOOR(RAND()*1000000)));