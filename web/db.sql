SET foreign_key_checks = 0;

DROP TABLE IF EXISTS users;
CREATE TABLE users
(
  id       int          NOT NULL AUTO_INCREMENT,
  username varchar(64)  NOT NULL UNIQUE,
  password varchar(255) NOT NULL,
  PRIMARY KEY (id)
)
  CHARACTER SET utf8
  COLLATE utf8_general_ci;

SET foreign_key_checks = 1;
