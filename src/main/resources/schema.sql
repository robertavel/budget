create table transactionData
(
 accountId varchar(50) not null,
 id UUID not null,
 date date not null,
 description varchar(50) not null,
 amount decimal not null
);
