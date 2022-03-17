DROP DATABASE if exists USCRecSports;

CREATE DATABASE USCRecSports;

USE USCRecSports;

CREATE TABLE StudentInfo (
  SID int primary key not null auto_increment,
  Name varchar(50) not null
);

INSERT INTO StudentInfo (SID, Name) VALUES (111, 'Jack Xu');
INSERT INTO StudentInfo (SID, Name) VALUES (112, 'Daniel Mizrahi');
INSERT INTO StudentInfo (SID, Name) VALUES (113, 'Emily Jin');

CREATE TABLE Grade (
  ID int primary key not null auto_increment,
  SID int not null,
  ClassName varchar(10) not null,
  Grade varchar(2) not null,
  FOREIGN KEY fk1(SID) REFERENCES StudentInfo(SID)
);

INSERT INTO Grade (SID, ClassName, Grade) VALUES (111, 'ART123', 'F');
INSERT INTO Grade (SID, ClassName, Grade) VALUES (111, 'BUS456', 'A-');
INSERT INTO Grade (SID, ClassName, Grade) VALUES (113, 'REL100', 'D-');
INSERT INTO Grade (SID, ClassName, Grade) VALUES (113, 'ECO966', 'A-');
INSERT INTO Grade (SID, ClassName, Grade) VALUES (113, 'BUS456', 'B+');
INSERT INTO Grade (SID, ClassName, Grade) VALUES (112, 'BUS456', 'A');
INSERT INTO Grade (SID, ClassName, Grade) VALUES (112, 'ECO966', 'B+');
