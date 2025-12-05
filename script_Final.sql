CREATE DATABASE IF NOT EXISTS SmartWorkSpaces;

USE smartworkspaces;

CREATE TABLE IF NOT EXISTS sala
(
	id_sale INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    capacidad INT NOT NULL,
	estado VARCHAR(25) NOT NULL
);



ALTER TABLE  sala
ADD CONSTRAINT restric_capacidad
CHECK (capacidad > 0);

ALTER TABLE  sala
ADD CONSTRAINT restric_estado
CHECK (estado IN("ACTIVA" , "INACTIVA"));

CREATE TABLE IF NOT EXISTS reserva
(
	id_reserva INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    usuario VARCHAR(100) NOT NULL,
    fecha DATE NOT NULL,
    horaInicio DATE NOT NULL,
    horaFin DATE NOT NULL,
    id_sale INT NOT NULL

);


ALTER TABLE reserva
ADD CONSTRAINT FK_sala
FOREIGN KEY(id_sale) REFERENCES sala(id_sale);