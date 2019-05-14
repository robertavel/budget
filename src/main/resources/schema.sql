create table accountTable
(
  id   varchar(50) not null,
  name varchar(50) not null,
  primary key (id),
);

create table transactionTable
(
  accountId   varchar(50) not null,
  id          varchar(50) not null,
  date        date        not null,
  description varchar(50) not null,
  amount      decimal     not null,
  primary key (id),
  foreign key (accountId) references accountTable (id) ON DELETE CASCADE
);
