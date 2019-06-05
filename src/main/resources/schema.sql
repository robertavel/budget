create table account
(
  id   UUID        NOT NULL,
  name VARCHAR(50) NOT NULL,
  PRIMARY KEY (id),
);

create table transactionTable
(
  accountId   UUID        NOT NULL,
  id          UUID        NOT NULL,
  date        DATE        NOT NULL,
  description VARCHAR(50) NOT NULL,
  amount      DECIMAL     NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (accountId) REFERENCES account (id) ON DELETE CASCADE
);
