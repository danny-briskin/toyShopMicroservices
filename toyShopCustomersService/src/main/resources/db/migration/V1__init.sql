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

CREATE TABLE st_users
(
    userId                  int         NOT NULL auto_increment PRIMARY KEY,
    firstName               varchar(50) NOT NULL,
    lastName                varchar(50) NOT NULL,
    email                   varchar(50) NOT NULL,
    password                varchar(250) NOT NULL,
    enabled                 boolean,
    created                 TIMESTAMP
);

CREATE TABLE st_roles
(
    roleId                  int         NOT NULL auto_increment PRIMARY KEY,
    roleName                varchar(50) NOT NULL,
    userId                  int,
    foreign key (userId) references st_users (userId)
);
