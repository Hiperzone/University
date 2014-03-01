DROP TABLE marcaAut;

CREATE TABLE marcaAut 
(Marca varchar(10), 
 Modelo varchar (15) primary key, 
 NmaxP integer check (NmaxP >0)
);

INSERT INTO marcaAut VALUES ('MAN', '13-220', 60);
INSERT INTO marcaAut VALUES ('Mercedes', 'The Citaro', 20);
INSERT INTO marcaAut VALUES ('IVECO', 'Eurorider 43', 45);


DROP TABLE autocarro;

CREATE TABLE autocarro 
(Matricula varchar (8) primary key,
 Modelo varchar(15),
 AnoMatricula integer,
 foreign key (Modelo) references marcaAut on delete restrict
);

INSERT INTO autocarro VALUES ('FH-76-MG','13-220',2001);
INSERT INTO autocarro VALUES ('SS-98-KL','13-220',1999);
INSERT INTO autocarro VALUES ('JT-65-BE','The Citaro',2001);
INSERT INTO autocarro VALUES ('EW-45-QQ','Eurorider 43',2003);
INSERT INTO autocarro VALUES ('PL-21-PO','Eurorider 43',2003);


DROP TABLE motorista;

CREATE TABLE motorista 
(NBi integer primary key,
 Nome varchar (50),
 Morada varchar (150)
);

INSERT INTO motorista VALUES(78906543,'Silva','Rua Grande Bl230 15 Esquerdo');
INSERT INTO motorista VALUES(12345678,'Santos','Rua Pequena Bl5 1 Direito');
INSERT INTO motorista VALUES(90876543,'Gomes','Rua Media N20 3 Direito');

DROP TABLE paragem;

CREATE TABLE paragem
(Paragem varchar(50) primary key
);

INSERT INTO paragem VALUES('Granito estrada da Igrejinha');
INSERT INTO paragem VALUES('Louredo');
INSERT INTO paragem VALUES('Sra dos Aflitos');
INSERT INTO paragem VALUES('Granito');
INSERT INTO paragem VALUES('Eb 2.3 Conde Vilalva');
INSERT INTO paragem VALUES('Lg Luis de Camoes');
INSERT INTO paragem VALUES('25 de Abril');
INSERT INTO paragem VALUES('St. Antonio');
INSERT INTO paragem VALUES('Comenda');
INSERT INTO paragem VALUES('Av Leonor Fernandes');
INSERT INTO paragem VALUES('Pc. Giraldes');
INSERT INTO paragem VALUES('ES Andre de Gouveia');
INSERT INTO paragem VALUES('Malagueira');
INSERT INTO paragem VALUES('Cartuxa');
INSERT INTO paragem VALUES('Vista Alegre');
INSERT INTO paragem VALUES('Nogueiras');


DROP TABLE percurso;

CREATE TABLE percurso
( CodigoP integer primary key,
  NomeP varchar(50),
  NmaxP integer check (Nmaxp>0) 
);

INSERT INTO percurso VALUES(21,'Louredo-Lg Luis de Camoes', 45);
INSERT INTO percurso VALUES(23,'25 de Abril-Malagueira',20); 

DROP TABLE passoPercurso;

CREATE TABLE passoPercurso
( CodigoP integer,
  Minutos time,
  Paragem varchar(50),
  primary key(CodigoP, Minutos, Paragem),
  foreign key (CodigoP) references percurso on delete restrict,
  foreign key (Paragem) references paragem on delete restrict
);

INSERT INTO passoPercurso VALUES(21,'06:20:00','Granito estrada da Igrejinha');
INSERT INTO passoPercurso VALUES(21,'07:20:00','Granito estrada da Igrejinha');
INSERT INTO passoPercurso VALUES(21,'07:33:00','Granito estrada da Igrejinha');
INSERT INTO passoPercurso VALUES(21,'11:45:00','Granito estrada da Igrejinha');
INSERT INTO passoPercurso VALUES(21,'13:45:00','Granito estrada da Igrejinha');
INSERT INTO passoPercurso VALUES(21,'15:45:00','Granito estrada da Igrejinha');
INSERT INTO passoPercurso VALUES(21,'18:33:00','Granito estrada da Igrejinha');
INSERT INTO passoPercurso VALUES(21,'19:33:00','Granito estrada da Igrejinha');
INSERT INTO passoPercurso VALUES(21,'06:26:00','Louredo');
INSERT INTO passoPercurso VALUES(21,'07:26:00','Louredo');
INSERT INTO passoPercurso VALUES(21,'07:39:00','Louredo');
INSERT INTO passoPercurso VALUES(21,'11:51:00','Louredo');
INSERT INTO passoPercurso VALUES(21,'13:51:00','Louredo');
INSERT INTO passoPercurso VALUES(21,'15:51:00','Louredo');
INSERT INTO passoPercurso VALUES(21,'18:39:00','Louredo');
INSERT INTO passoPercurso VALUES(21,'19:39:00','Louredo');
INSERT INTO passoPercurso VALUES(21,'06:33:00','Sra dos Aflitos');
INSERT INTO passoPercurso VALUES(21,'07:32:00','Sra dos Aflitos');
INSERT INTO passoPercurso VALUES(21,'07:45:00','Sra dos Aflitos');
INSERT INTO passoPercurso VALUES(21,'11:57:00','Sra dos Aflitos');
INSERT INTO passoPercurso VALUES(21,'13:57:00','Sra dos Aflitos');
INSERT INTO passoPercurso VALUES(21,'15:57:00','Sra dos Aflitos');
INSERT INTO passoPercurso VALUES(21,'18:45:00','Sra dos Aflitos');
INSERT INTO passoPercurso VALUES(21,'19:45:00','Sra dos Aflitos');
INSERT INTO passoPercurso VALUES(21,'06:40:00','Granito');
INSERT INTO passoPercurso VALUES(21,'07:40:00','Granito');
INSERT INTO passoPercurso VALUES(21,'07:53:00','Granito');
INSERT INTO passoPercurso VALUES(21,'08:42:00','Granito');
INSERT INTO passoPercurso VALUES(21,'09:29:00','Granito');
INSERT INTO passoPercurso VALUES(21,'10:55:00','Granito');
INSERT INTO passoPercurso VALUES(21,'12:05:00','Granito');
INSERT INTO passoPercurso VALUES(21,'14:05:00','Granito');
INSERT INTO passoPercurso VALUES(21,'15:10:00','Granito');
INSERT INTO passoPercurso VALUES(21,'16:05:00','Granito');
INSERT INTO passoPercurso VALUES(21,'17:05:00','Granito');
INSERT INTO passoPercurso VALUES(21,'18:05:00','Granito');
INSERT INTO passoPercurso VALUES(21,'18:53:00','Granito');
INSERT INTO passoPercurso VALUES(21,'19:53:00','Granito');
INSERT INTO passoPercurso VALUES(21,'06:47:00','Eb 2.3 Conde Vilalva');
INSERT INTO passoPercurso VALUES(21,'07:47:00','Eb 2.3 Conde Vilalva');
INSERT INTO passoPercurso VALUES(21,'08:00:00','Eb 2.3 Conde Vilalva');
INSERT INTO passoPercurso VALUES(21,'08:49:00','Eb 2.3 Conde Vilalva');
INSERT INTO passoPercurso VALUES(21,'09:40:00','Eb 2.3 Conde Vilalva');
INSERT INTO passoPercurso VALUES(21,'11:02:00','Eb 2.3 Conde Vilalva');
INSERT INTO passoPercurso VALUES(21,'12:12:00','Eb 2.3 Conde Vilalva');
INSERT INTO passoPercurso VALUES(21,'14:12:00','Eb 2.3 Conde Vilalva');
INSERT INTO passoPercurso VALUES(21,'15:22:00','Eb 2.3 Conde Vilalva');
INSERT INTO passoPercurso VALUES(21,'16:12:00','Eb 2.3 Conde Vilalva');
INSERT INTO passoPercurso VALUES(21,'17:12:00','Eb 2.3 Conde Vilalva');
INSERT INTO passoPercurso VALUES(21,'18:12:00','Eb 2.3 Conde Vilalva');
INSERT INTO passoPercurso VALUES(21,'19:00:00','Eb 2.3 Conde Vilalva');
INSERT INTO passoPercurso VALUES(21,'20:00:00','Eb 2.3 Conde Vilalva');
INSERT INTO passoPercurso VALUES(21,'06:54:00','Lg Luis de Camoes');
INSERT INTO passoPercurso VALUES(21,'07:54:00','Lg Luis de Camoes');
INSERT INTO passoPercurso VALUES(21,'08:07:00','Lg Luis de Camoes');
INSERT INTO passoPercurso VALUES(21,'08:57:00','Lg Luis de Camoes');
INSERT INTO passoPercurso VALUES(21,'09:47:00','Lg Luis de Camoes');
INSERT INTO passoPercurso VALUES(21,'11:09:00','Lg Luis de Camoes');
INSERT INTO passoPercurso VALUES(21,'12:19:00','Lg Luis de Camoes');
INSERT INTO passoPercurso VALUES(21,'14:19:00','Lg Luis de Camoes');
INSERT INTO passoPercurso VALUES(21,'15:29:00','Lg Luis de Camoes');
INSERT INTO passoPercurso VALUES(21,'16:19:00','Lg Luis de Camoes');
INSERT INTO passoPercurso VALUES(21,'17:19:00','Lg Luis de Camoes');
INSERT INTO passoPercurso VALUES(21,'18:19:00','Lg Luis de Camoes');
INSERT INTO passoPercurso VALUES(21,'19:07:00','Lg Luis de Camoes');
INSERT INTO passoPercurso VALUES(21,'20:07:00','Lg Luis de Camoes');
INSERT INTO passoPercurso VALUES(21,'08:13:00','Granito');
INSERT INTO passoPercurso VALUES(21,'09:03:00','Granito');
INSERT INTO passoPercurso VALUES(21,'10:03:00','Granito');
INSERT INTO passoPercurso VALUES(21,'12:25:00','Granito');
INSERT INTO passoPercurso VALUES(21,'14:25:00','Granito');
INSERT INTO passoPercurso VALUES(21,'15:35:00','Granito');
INSERT INTO passoPercurso VALUES(21,'16:25:00','Granito');
INSERT INTO passoPercurso VALUES(21,'17:25:00','Granito');
INSERT INTO passoPercurso VALUES(21,'18:25:00','Granito');
INSERT INTO passoPercurso VALUES(21,'19:13:00','Granito');
INSERT INTO passoPercurso VALUES(21,'20:13:00','Granito');
INSERT INTO passoPercurso VALUES(21,'08:18:00','Eb 2.3 Conde Vilalva');
INSERT INTO passoPercurso VALUES(21,'09:08:00','Eb 2.3 Conde Vilalva');
INSERT INTO passoPercurso VALUES(21,'10:08:00','Eb 2.3 Conde Vilalva');
INSERT INTO passoPercurso VALUES(21,'12:30:00','Eb 2.3 Conde Vilalva');
INSERT INTO passoPercurso VALUES(21,'14:30:00','Eb 2.3 Conde Vilalva');
INSERT INTO passoPercurso VALUES(21,'15:40:00','Eb 2.3 Conde Vilalva');
INSERT INTO passoPercurso VALUES(21,'16:30:00','Eb 2.3 Conde Vilalva');
INSERT INTO passoPercurso VALUES(21,'19:18:00','Eb 2.3 Conde Vilalva');
INSERT INTO passoPercurso VALUES(21,'20:18:00','Eb 2.3 Conde Vilalva');
INSERT INTO passoPercurso VALUES(21,'08:27:00','Louredo');
INSERT INTO passoPercurso VALUES(21,'09:17:00','Louredo');
INSERT INTO passoPercurso VALUES(21,'10:17:00','Louredo');
INSERT INTO passoPercurso VALUES(21,'12:39:00','Louredo');
INSERT INTO passoPercurso VALUES(21,'14:39:00','Louredo');
INSERT INTO passoPercurso VALUES(21,'15:49:00','Louredo');
INSERT INTO passoPercurso VALUES(21,'16:39:00','Louredo');
INSERT INTO passoPercurso VALUES(21,'19:27:00','Louredo');
INSERT INTO passoPercurso VALUES(21,'20:27:00','Louredo');
INSERT INTO passoPercurso VALUES(21,'08:34:00','Sra dos Aflitos');
INSERT INTO passoPercurso VALUES(21,'09:24:00','Sra dos Aflitos');
INSERT INTO passoPercurso VALUES(21,'10:24:00','Sra dos Aflitos');
INSERT INTO passoPercurso VALUES(21,'12:46:00','Sra dos Aflitos');
INSERT INTO passoPercurso VALUES(21,'14:46:00','Sra dos Aflitos');
INSERT INTO passoPercurso VALUES(21,'15:56:00','Sra dos Aflitos');
INSERT INTO passoPercurso VALUES(21,'16:46:00','Sra dos Aflitos');
INSERT INTO passoPercurso VALUES(21,'19:34:00','Sra dos Aflitos');
INSERT INTO passoPercurso VALUES(21,'20:34:00','Sra dos Aflitos');
INSERT INTO passoPercurso VALUES(21,'08:39:00','Granito');
INSERT INTO passoPercurso VALUES(21,'10:29:00','Granito');
INSERT INTO passoPercurso VALUES(21,'12:51:00','Granito');
INSERT INTO passoPercurso VALUES(21,'14:51:00','Granito');
INSERT INTO passoPercurso VALUES(21,'16:01:00','Granito');
INSERT INTO passoPercurso VALUES(21,'16:51:00','Granito');
INSERT INTO passoPercurso VALUES(21,'19:39:00','Granito');
INSERT INTO passoPercurso VALUES(21,'20:39:00','Granito');

INSERT INTO passoPercurso VALUES(23,'06:40:00','25 de Abril');
INSERT INTO passoPercurso VALUES(23,'07:40:00','25 de Abril');
INSERT INTO passoPercurso VALUES(23,'07:58:00','25 de Abril');
INSERT INTO passoPercurso VALUES(23,'08:40:00','25 de Abril');
INSERT INTO passoPercurso VALUES(23,'08:58:00','25 de Abril');
INSERT INTO passoPercurso VALUES(23,'09:20:00','25 de Abril');
INSERT INTO passoPercurso VALUES(23,'11:20:00','25 de Abril');
INSERT INTO passoPercurso VALUES(23,'12:58:00','25 de Abril');
INSERT INTO passoPercurso VALUES(23,'13:20:00','25 de Abril');
INSERT INTO passoPercurso VALUES(23,'14:58:00','25 de Abril');
INSERT INTO passoPercurso VALUES(23,'15:20:00','25 de Abril');
INSERT INTO passoPercurso VALUES(23,'17:20:00','25 de Abril');
INSERT INTO passoPercurso VALUES(23,'06:42:00','St. Antonio');
INSERT INTO passoPercurso VALUES(23,'07:42:00','St. Antonio');
INSERT INTO passoPercurso VALUES(23,'08:00:00','St. Antonio');
INSERT INTO passoPercurso VALUES(23,'08:42:00','St. Antonio');
INSERT INTO passoPercurso VALUES(23,'09:00:00','St. Antonio');
INSERT INTO passoPercurso VALUES(23,'09:22:00','St. Antonio');
INSERT INTO passoPercurso VALUES(23,'11:22:00','St. Antonio');
INSERT INTO passoPercurso VALUES(23,'13:00:00','St. Antonio');
INSERT INTO passoPercurso VALUES(23,'13:22:00','St. Antonio');
INSERT INTO passoPercurso VALUES(23,'15:00:00','St. Antonio');
INSERT INTO passoPercurso VALUES(23,'15:22:00','St. Antonio');
INSERT INTO passoPercurso VALUES(23,'17:22:00','St. Antonio');
INSERT INTO passoPercurso VALUES(23,'06:44:00','Comenda');
INSERT INTO passoPercurso VALUES(23,'07:44:00','Comenda');
INSERT INTO passoPercurso VALUES(23,'08:02:00','Comenda');
INSERT INTO passoPercurso VALUES(23,'08:44:00','Comenda');
INSERT INTO passoPercurso VALUES(23,'09:02:00','Comenda');
INSERT INTO passoPercurso VALUES(23,'09:24:00','Comenda');
INSERT INTO passoPercurso VALUES(23,'11:24:00','Comenda');
INSERT INTO passoPercurso VALUES(23,'13:02:00','Comenda');
INSERT INTO passoPercurso VALUES(23,'13:24:00','Comenda');
INSERT INTO passoPercurso VALUES(23,'15:02:00','Comenda');
INSERT INTO passoPercurso VALUES(23,'15:24:00','Comenda');
INSERT INTO passoPercurso VALUES(23,'17:24:00','Comenda');
INSERT INTO passoPercurso VALUES(23,'06:47:00','Av Leonor Fernandes');
INSERT INTO passoPercurso VALUES(23,'07:47:00','Av Leonor Fernandes');
INSERT INTO passoPercurso VALUES(23,'08:04:00','Av Leonor Fernandes');
INSERT INTO passoPercurso VALUES(23,'08:47:00','Av Leonor Fernandes');
INSERT INTO passoPercurso VALUES(23,'09:04:00','Av Leonor Fernandes');
INSERT INTO passoPercurso VALUES(23,'09:27:00','Av Leonor Fernandes');
INSERT INTO passoPercurso VALUES(23,'11:27:00','Av Leonor Fernandes');
INSERT INTO passoPercurso VALUES(23,'13:04:00','Av Leonor Fernandes');
INSERT INTO passoPercurso VALUES(23,'13:27:00','Av Leonor Fernandes');
INSERT INTO passoPercurso VALUES(23,'15:04:00','Av Leonor Fernandes');
INSERT INTO passoPercurso VALUES(23,'15:27:00','Av Leonor Fernandes');
INSERT INTO passoPercurso VALUES(23,'17:27:00','Av Leonor Fernandes');
INSERT INTO passoPercurso VALUES(23,'06:54:00','Pc. Giraldes');
INSERT INTO passoPercurso VALUES(23,'07:54:00','Pc. Giraldes');
INSERT INTO passoPercurso VALUES(23,'08:07:00','Pc. Giraldes');
INSERT INTO passoPercurso VALUES(23,'08:54:00','Pc. Giraldes');
INSERT INTO passoPercurso VALUES(23,'09:07:00','Pc. Giraldes');
INSERT INTO passoPercurso VALUES(23,'09:34:00','Pc. Giraldes');
INSERT INTO passoPercurso VALUES(23,'11:34:00','Pc. Giraldes');
INSERT INTO passoPercurso VALUES(23,'13:07:00','Pc. Giraldes');
INSERT INTO passoPercurso VALUES(23,'13:34:00','Pc. Giraldes');
INSERT INTO passoPercurso VALUES(23,'15:07:00','Pc. Giraldes');
INSERT INTO passoPercurso VALUES(23,'15:34:00','Pc. Giraldes');
INSERT INTO passoPercurso VALUES(23,'17:34:00','Pc. Giraldes');
INSERT INTO passoPercurso VALUES(23,'08:00:00','ES Andre de Gouveia');
INSERT INTO passoPercurso VALUES(23,'09:00:00','ES Andre de Gouveia');
INSERT INTO passoPercurso VALUES(23,'09:40:00','ES Andre de Gouveia');
INSERT INTO passoPercurso VALUES(23,'11:40:00','ES Andre de Gouveia');
INSERT INTO passoPercurso VALUES(23,'13:40:00','ES Andre de Gouveia');
INSERT INTO passoPercurso VALUES(23,'15:40:00','ES Andre de Gouveia');
INSERT INTO passoPercurso VALUES(23,'17:40:00','ES Andre de Gouveia');
INSERT INTO passoPercurso VALUES(23,'08:02:00','Malagueira');
INSERT INTO passoPercurso VALUES(23,'09:02:00','Malagueira');
INSERT INTO passoPercurso VALUES(23,'09:42:00','Malagueira');
INSERT INTO passoPercurso VALUES(23,'11:42:00','Malagueira');
INSERT INTO passoPercurso VALUES(23,'13:42:00','Malagueira');
INSERT INTO passoPercurso VALUES(23,'15:42:00','Malagueira');
INSERT INTO passoPercurso VALUES(23,'17:42:00','Malagueira');
INSERT INTO passoPercurso VALUES(23,'08:06:00','Cartuxa');
INSERT INTO passoPercurso VALUES(23,'09:06:00','Cartuxa');
INSERT INTO passoPercurso VALUES(23,'09:44:00','Cartuxa');
INSERT INTO passoPercurso VALUES(23,'11:44:00','Cartuxa');
INSERT INTO passoPercurso VALUES(23,'13:44:00','Cartuxa');
INSERT INTO passoPercurso VALUES(23,'15:44:00','Cartuxa');
INSERT INTO passoPercurso VALUES(23,'17:44:00','Cartuxa');
INSERT INTO passoPercurso VALUES(23,'08:08:00','Vista Alegre');
INSERT INTO passoPercurso VALUES(23,'09:08:00','Vista Alegre');
INSERT INTO passoPercurso VALUES(23,'09:46:00','Vista Alegre');
INSERT INTO passoPercurso VALUES(23,'11:46:00','Vista Alegre');
INSERT INTO passoPercurso VALUES(23,'13:46:00','Vista Alegre');
INSERT INTO passoPercurso VALUES(23,'15:46:00','Vista Alegre');
INSERT INTO passoPercurso VALUES(23,'17:46:00','Vista Alegre');
INSERT INTO passoPercurso VALUES(23,'08:14:00','Lg Luis de Camoes');
INSERT INTO passoPercurso VALUES(23,'09:14:00','Lg Luis de Camoes');
INSERT INTO passoPercurso VALUES(23,'09:52:00','Lg Luis de Camoes');
INSERT INTO passoPercurso VALUES(23,'11:52:00','Lg Luis de Camoes');
INSERT INTO passoPercurso VALUES(23,'13:52:00','Lg Luis de Camoes');
INSERT INTO passoPercurso VALUES(23,'15:52:00','Lg Luis de Camoes');
INSERT INTO passoPercurso VALUES(23,'17:52:00','Lg Luis de Camoes');
INSERT INTO passoPercurso VALUES(23,'08:19:00','Nogueiras');
INSERT INTO passoPercurso VALUES(23,'09:19:00','Nogueiras');
INSERT INTO passoPercurso VALUES(23,'09:57:00','Nogueiras');
INSERT INTO passoPercurso VALUES(23,'11:57:00','Nogueiras');
INSERT INTO passoPercurso VALUES(23,'13:57:00','Nogueiras');
INSERT INTO passoPercurso VALUES(23,'15:57:00','Nogueiras');
INSERT INTO passoPercurso VALUES(23,'17:57:00','Nogueiras');
INSERT INTO passoPercurso VALUES(23,'08:24:00','25 de Abril');
INSERT INTO passoPercurso VALUES(23,'09:24:00','25 de Abril');
INSERT INTO passoPercurso VALUES(23,'10:02:00','25 de Abril');
INSERT INTO passoPercurso VALUES(23,'12:02:00','25 de Abril');
INSERT INTO passoPercurso VALUES(23,'14:02:00','25 de Abril');
INSERT INTO passoPercurso VALUES(23,'16:02:00','25 de Abril');
INSERT INTO passoPercurso VALUES(23,'18:02:00','25 de Abril');


DROP TABLE fezPercurso;

CREATE TABLE fezPercurso
( CodigoP integer,
  NBi Integer,
  Matricula char(8),
  DataInicio timestamp,
  DataFim timestamp,
  primary key(NBi,DataInicio),
  foreign key(CodigoP) references percurso on delete restrict,
  foreign key(NBi) references motorista on delete restrict,
  foreign key(Matricula) references autocarro on delete restrict);


INSERT INTO fezPercurso VALUES(21, 90876543, 'FH-76-MG', '2009-11-12 06:20:00', '2009-11-12 06:54:00');
INSERT INTO fezPercurso VALUES(23, 78906543, 'SS-98-KL', '2009-11-12 11:20:00', '2009-11-12 12:02:00');

DROP TABLE bilhete;

CREATE TABLE bilhete
( NBi integer,
  DataInicio timestamp,
  DataPass timestamp,
  Paragem varchar(50),
  primary key(NBi,DataPass),
  foreign key(NBi) references motorista on delete restrict,
  foreign key(Nbi,DataInicio) references fezPercurso on delete restrict,
  foreign key(Paragem) references paragem on delete restrict);

INSERT INTO bilhete VALUES(90876543,'2009-11-12 06:20:00', '2009-11-12 06:20:00', 'Granito estrada da Igrejinha');
INSERT INTO bilhete VALUES(90876543,'2009-11-12 06:20:00', '2009-11-12 06:20:01', 'Granito estrada da Igrejinha');
INSERT INTO bilhete VALUES(90876543,'2009-11-12 06:20:00', '2009-11-12 06:20:03', 'Granito estrada da Igrejinha');
INSERT INTO bilhete VALUES(90876543,'2009-11-12 06:20:00', '2009-11-12 06:20:04', 'Granito estrada da Igrejinha');
INSERT INTO bilhete VALUES(90876543,'2009-11-12 06:20:00', '2009-11-12 06:20:06', 'Granito estrada da Igrejinha');
INSERT INTO bilhete VALUES(90876543,'2009-11-12 06:20:00', '2009-11-12 06:26:00', 'Louredo');
INSERT INTO bilhete VALUES(90876543,'2009-11-12 06:20:00', '2009-11-12 06:26:03', 'Louredo');
INSERT INTO bilhete VALUES(90876543,'2009-11-12 06:20:00', '2009-11-12 06:26:06', 'Louredo');
INSERT INTO bilhete VALUES(90876543,'2009-11-12 06:20:00', '2009-11-12 06:33:00', 'Sra dos Aflitos');
INSERT INTO bilhete VALUES(90876543,'2009-11-12 06:20:00', '2009-11-12 06:40:00', 'Granito');
INSERT INTO bilhete VALUES(90876543,'2009-11-12 06:20:00', '2009-11-12 06:40:01', 'Granito');
INSERT INTO bilhete VALUES(90876543,'2009-11-12 06:20:00', '2009-11-12 06:40:02', 'Granito');
INSERT INTO bilhete VALUES(90876543,'2009-11-12 06:20:00', '2009-11-12 06:40:03', 'Granito');
INSERT INTO bilhete VALUES(90876543,'2009-11-12 06:20:00', '2009-11-12 06:47:00', 'Eb 2.3 Conde Vilalva');
INSERT INTO bilhete VALUES(90876543,'2009-11-12 06:20:00', '2009-11-12 06:47:01', 'Eb 2.3 Conde Vilalva');
INSERT INTO bilhete VALUES(90876543,'2009-11-12 06:20:00', '2009-11-12 06:47:02', 'Eb 2.3 Conde Vilalva');
INSERT INTO bilhete VALUES(90876543,'2009-11-12 06:20:00', '2009-11-12 06:47:03', 'Eb 2.3 Conde Vilalva');
INSERT INTO bilhete VALUES(90876543,'2009-11-12 06:20:00', '2009-11-12 06:47:04', 'Eb 2.3 Conde Vilalva');

INSERT INTO bilhete VALUES(78906543,'2009-11-12 11:20:00', '2009-11-12 11:20:00', '25 de Abril');
INSERT INTO bilhete VALUES(78906543,'2009-11-12 11:20:00', '2009-11-12 11:20:01', '25 de Abril');
INSERT INTO bilhete VALUES(78906543,'2009-11-12 11:20:00', '2009-11-12 11:20:02', '25 de Abril');
INSERT INTO bilhete VALUES(78906543,'2009-11-12 11:20:00', '2009-11-12 11:20:03', '25 de Abril');
INSERT INTO bilhete VALUES(78906543,'2009-11-12 11:20:00', '2009-11-12 11:22:00', 'St. Antonio');
INSERT INTO bilhete VALUES(78906543,'2009-11-12 11:20:00', '2009-11-12 11:22:01', 'St. Antonio');
INSERT INTO bilhete VALUES(78906543,'2009-11-12 11:20:00', '2009-11-12 11:22:02', 'St. Antonio');
INSERT INTO bilhete VALUES(78906543,'2009-11-12 11:20:00', '2009-11-12 11:22:03', 'St. Antonio');
INSERT INTO bilhete VALUES(78906543,'2009-11-12 11:20:00', '2009-11-12 11:22:04', 'St. Antonio');
INSERT INTO bilhete VALUES(78906543,'2009-11-12 11:20:00', '2009-11-12 11:24:00', 'Comenda');
INSERT INTO bilhete VALUES(78906543,'2009-11-12 11:20:00', '2009-11-12 11:27:00', 'Av Leonor Fernandes');
INSERT INTO bilhete VALUES(78906543,'2009-11-12 11:20:00', '2009-11-12 11:27:01', 'Av Leonor Fernandes');
INSERT INTO bilhete VALUES(78906543,'2009-11-12 11:20:00', '2009-11-12 11:27:02', 'Av Leonor Fernandes');
INSERT INTO bilhete VALUES(78906543,'2009-11-12 11:20:00', '2009-11-12 11:34:00', 'Pc. Giraldes');
INSERT INTO bilhete VALUES(78906543,'2009-11-12 11:20:00', '2009-11-12 11:42:00', 'Malagueira');
INSERT INTO bilhete VALUES(78906543,'2009-11-12 11:20:00', '2009-11-12 11:42:01', 'Malagueira');
INSERT INTO bilhete VALUES(78906543,'2009-11-12 11:20:00', '2009-11-12 11:42:02', 'Malagueira');
INSERT INTO bilhete VALUES(78906543,'2009-11-12 11:20:00', '2009-11-12 11:44:00', 'Cartuxa');
INSERT INTO bilhete VALUES(78906543,'2009-11-12 11:20:00', '2009-11-12 11:44:01', 'Cartuxa');
INSERT INTO bilhete VALUES(78906543,'2009-11-12 11:20:00', '2009-11-12 11:44:02', 'Cartuxa');
INSERT INTO bilhete VALUES(78906543,'2009-11-12 11:20:00', '2009-11-12 11:44:03', 'Cartuxa');
INSERT INTO bilhete VALUES(78906543,'2009-11-12 11:20:00', '2009-11-12 11:44:04', 'Cartuxa');
INSERT INTO bilhete VALUES(78906543,'2009-11-12 11:20:00', '2009-11-12 11:46:00', 'Vista Alegre');
INSERT INTO bilhete VALUES(78906543,'2009-11-12 11:20:00', '2009-11-12 11:46:01', 'Vista Alegre');
INSERT INTO bilhete VALUES(78906543,'2009-11-12 11:20:00', '2009-11-12 11:46:02', 'Vista Alegre');
INSERT INTO bilhete VALUES(78906543,'2009-11-12 11:20:00', '2009-11-12 11:57:00', 'Nogueiras');
INSERT INTO bilhete VALUES(78906543,'2009-11-12 11:20:00', '2009-11-12 11:57:01', 'Nogueiras'); 
INSERT INTO bilhete VALUES(78906543,'2009-11-12 11:20:00', '2009-11-12 11:57:02', 'Nogueiras');
INSERT INTO bilhete VALUES(78906543,'2009-11-12 11:20:00', '2009-11-12 11:57:03', 'Nogueiras');
INSERT INTO bilhete VALUES(78906543,'2009-11-12 11:20:00', '2009-11-12 11:57:04', 'Nogueiras');
INSERT INTO bilhete VALUES(78906543,'2009-11-12 11:20:00', '2009-11-12 11:57:05', 'Nogueiras');
