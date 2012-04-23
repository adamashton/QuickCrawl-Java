create user 'web'@'localhost' IDENTIFIED BY 'web';

drop database if exists QuickCrawl;
create database QuickCrawl;

grant ALL on QuickCrawl.* to web@localhost; 
flush privileges;

use QuickCrawl;

create table users
(
	user varchar(128),
	password varchar(128),
	email varchar(128)
);

create table crawls
(
	id varchar(10),
	user varchar(128),
	manager blob,
	log mediumblob
);

