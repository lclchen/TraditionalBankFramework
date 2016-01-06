create database banksystem;
use banksystem;

create table if not exists account (
	account_id varchar(30) primary key not null unique,
	username varchar(50) not  null,
	balance varchar(30) not null,
	currency varchar(20) not null,
	activated boolean not null,
	revision integer not null
);


