DROP TABLE  book IF EXISTS;
create table book
(
  id INTEGER IDENTITY PRIMARY KEY,
  title VARCHAR(128),
  image VARCHAR(256),
  author VARCHAR(64),
  price DECIMAL(18,2)
);
