CREATE TABLE cliente (
  Id int(11) NOT NULL AUTO_INCREMENT,
  Nome varchar(60) DEFAULT NULL,
  PRIMARY KEY (Id)
);

CREATE TABLE funcionario (
  Id int(11) NOT NULL AUTO_INCREMENT,
  Nome varchar(60) NOT NULL,
  Email varchar(100) NOT NULL,
  DataAniversario datetime NOT NULL,
  SalarioBase double NOT NULL,
  ClienteId int(11) NOT NULL,
  PRIMARY KEY (Id),
  FOREIGN KEY (ClienteId) REFERENCES cliente (id)
);

CREATE TABLE produto (
	Id int(11) NOT NULL AUTO_INCREMENT,
    Nome varchar(60) NOT NULL,
    Preco double NOT NULL,
	Quantidade int(11) NOT NULL,
    PRIMARY KEY (Id)
);

INSERT INTO cliente (Nome) VALUES 
  ('Ana'),
  ('Maria'),
  ('Nina'),
  ('Pedro');

INSERT INTO funcionario (Nome, Email, DataAniversario, SalarioBase, ClienteId) VALUES 
  ('Bob Brown','bob@gmail.com','1998-04-21 00:00:00',1000,1),
  ('Maria Green','maria@gmail.com','1979-12-31 00:00:00',3500,2),
  ('Alex Grey','alex@gmail.com','1988-01-15 00:00:00',2200,1),
  ('Martha Red','martha@gmail.com','1993-11-30 00:00:00',3000,4),
  ('Donald Blue','donald@gmail.com','2000-01-09 00:00:00',4000,3),
  ('Alex Pink','bob@gmail.com','1997-03-04 00:00:00',3000,2);
  
INSERT INTO produto (Nome, Preco, Quantidade) VALUES
	('A Seleção', 35, 5),
    ('O Acordo', 65, 9),
    ('O Duque e Eu', 40, 7),
    ('Madame Bovary', 10, 6),
    ('Eleanor e Park', 9, 4);