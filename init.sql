create table rooms
(
    rid          bigint not null
        primary key,
    description  text,
    is_public    boolean,
    is_traceless boolean
);
create table room_users
(
    rid bigint not null,
    uid bigint not null,
    primary key (rid, uid)
);
create table messages
(
    mid                 bigint  not null
        primary key,
    content             text,
    created_at          bigint,
    is_enablehtml       boolean not null,
    is_enable_mark_down boolean not null,
    rid                 bigint  not null,
    uid                 bigint
);
create table user_password
(
    id       BIGINT not null
        primary key,
    salt     VARCHAR(32),
    password VARCHAR(32),
    uid      BIGINT not null
        unique
        references users
);
create table users
(
    uid         bigint       not null
        primary key,
    created_at  bigint,
    description text,
    email       varchar(255),
    is_disable  boolean,
    name        varchar(255) not null
        unique
);
insert into rooms(rid, description, is_public, is_traceless)
values (0,'公共大厅',true,false)