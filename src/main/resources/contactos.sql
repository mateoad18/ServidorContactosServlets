DROP DATABASE IF EXISTS contactos;
CREATE DATABASE contactos;
USE `contactos`;

CREATE TABLE contactos (
  nombre varchar(256) PRIMARY KEY,
  email varchar(256) DEFAULT NULL
) ENGINE=InnoDB;

INSERT INTO contactos VALUES
('Pepe', 'notengo@mail.com'),
('Pepín', 'tampocotengo@mail.com');

CREATE TABLE telefonos (
  numero varchar(20),
  contacto varchar(256),
  PRIMARY KEY (numero, contacto),
  CONSTRAINT fk_telefonos_contactos
  FOREIGN KEY (contacto) REFERENCES contactos (nombre)
  ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

INSERT INTO telefonos VALUES
('612 010 101', 'Pepe'),
('622 111 222', 'Pepe'),
('633 333 555', 'Pepe'),
('655 123 456', 'Pepín'),
('677 321 123', 'Pepín');

