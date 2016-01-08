create database banksystem;
use banksystem;

create table if not exists account (
	account_id varchar(50) primary key not null unique,
	username varchar(50) not  null,
	balance varchar(30) not null,
	currency varchar(20) not null,
	activated boolean not null,
	revision integer not null
);

create table if not exists event (
  event_id varchar(50) primary key not null unique,
  command_id varchar(50) not null,
  committed_time varchar(50) not null,
  account_id varchar(50) not null,
  currency varchar(20) not null,
  revision integer not null,
  type varchar(20) not null,
  account_transfer_in varchar(30),
  account_transfer_out varchar(30),
  amount varchar(30)
);

alter table event add INDEX account_id_index (account_id);
