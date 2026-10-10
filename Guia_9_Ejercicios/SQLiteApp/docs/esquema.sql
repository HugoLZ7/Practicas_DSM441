-- Esquema de tienda.sqlite (equivale a lo que crea HelperDB.onCreate)
PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS categoria(
  idcategoria integer primary key autoincrement,
  nombre varchar(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS productos(
  idproductos integer primary key autoincrement,
  idcategoria integer NOT NULL,
  descripcion varchar(150) NOT NULL,
  precio decimal(10,2) NOT NULL,
  cantidad integer,
  FOREIGN KEY(idcategoria) REFERENCES categoria(idcategoria)
);

CREATE TABLE IF NOT EXISTS usuario(
  idusuario integer primary key autoincrement,
  nick varchar(30) NOT NULL UNIQUE COLLATE NOCASE,
  password varchar(100) NOT NULL,   -- hash PBKDF2 (hex)
  salt varchar(40) NOT NULL         -- sal aleatoria (hex)
);
