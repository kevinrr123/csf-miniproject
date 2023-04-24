
drop database if exists csfproject;

create database csfproject;

use csfproject;

create table users(
    -- primary key
    userid int NOT NULL AUTO_INCREMENT,
    username varchar(128) not null,
    password varchar(256) not null,

    primary key(userid)

);

create table contactinfo(
    -- primary key
    contactid int NOT NULL AUTO_INCREMENT,
    user_id int not null,
    email varchar(256) not null,

    primary key(contactid),

    constraint fk_user_id
        foreign key(user_id)
        references users(userid)
        on delete cascade
        on update cascade

);

create table commenttable(
    -- primary key
    recipe_id varchar(128) NOT NULL,
    userid int not null,
    recipename varchar(256) not null,
    commentitem varchar(1024),
    dates varchar(256) not null,

    primary key(recipe_id),

    constraint fk_userid
        foreign key(userid)
        references users(userid)
        on delete cascade
        on update cascade

);
