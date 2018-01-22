SET MODE PostgreSQL;

CREATE TABLE IF NOT EXISTS speakers (
id int PRIMARY KEY auto_increment,
firstName VARCHAR,
lastName VARCHAR,
eventId INTEGER,
background VARCHAR
);

CREATE TABLE IF NOT EXISTS events (
id int PRIMARY KEY auto_increment,
name VARCHAR,
description VARCHAR
);
