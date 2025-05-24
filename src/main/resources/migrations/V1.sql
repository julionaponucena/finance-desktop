CREATE TABLE block_registers(
    id_block_register INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    title VARCHAR NOT NULL
);

CREATE TABLE registers(
  id_register INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  id_block_register INT NOT NULL,
  title varchar NOT NULL,
  date varchar NOT NULL ,
  value_cents INT NOT NULL,
  FOREIGN KEY (id_block_register) REFERENCES block_registers(id_block_register)
);

CREATE TABLE categories(
    id_category INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    name VARCHAR NOT NULL
);

CREATE TABLE registers_categories(
    id_register INT NOT NULL,
    id_category INT NOT NULL,
    PRIMARY KEY(id_register,id_category),
    FOREIGN KEY (id_register) REFERENCES registers(id_register),
    FOREIGN KEY (id_category) REFERENCES categories(id_category)
);