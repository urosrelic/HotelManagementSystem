create schema hotel collate utf8mb4_general_ci;

create table users
(
    id                int auto_increment
        primary key,
    name              varchar(255) not null,
    email             varchar(255) not null,
    password          varchar(255) not null,
    security_question varchar(255) not null,
    answer            varchar(255) not null,
    address           varchar(255) not null,
    status            int          not null
);

create table rooms
(
    room_num  int            not null
        primary key,
    room_type varchar(255)   not null,
    bed       varchar(255)   not null,
    price     decimal(16, 2) not null,
    status    int            null
);

create table customers
(
    id             int auto_increment
        primary key,
    name           varchar(255)   not null,
    email          varchar(255)   not null,
    mobile_number  varchar(255)   not null,
    address        varchar(255)   not null,
    check_in_date  varchar(255)   not null,
    room_number    varchar(255)   not null,
    room_type      varchar(255)   not null,
    bed            varchar(255)   not null,
    price_per_day  decimal(16, 2) not null,
    days_staying   int(10)        null,
    total_amount   decimal(16, 2) null,
    check_out_date varchar(50)    null,
    paid           varchar(255)   null
);