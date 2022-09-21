CREATE TABLE customers
(
    id                  int         NOT NULL auto_increment PRIMARY KEY,
    name                varchar(50) NOT NULL,
    billingAddress      varchar(150),
    customerBalance     double,
    customerActivated   TIMESTAMP   NOT NULL,
    customerDeactivated TIMESTAMP
);

CREATE TABLE payments
(
    id            int       NOT NULL auto_increment PRIMARY KEY,
    paymentDate   TIMESTAMP NOT NULL,
    paymentAmount double,
    channel       varchar(30),
    customerID    int       NOT NULL,
    foreign key (customerID) references customers (ID)
);