--liquibase formatted sql

--changeset catunderglue:1
create table volunteers
(
    telegram_id bigint not null primary key,
    first_name  varchar(255),
    last_name   varchar(255)
);

create table trial_periods
(
    id               bigserial primary key,
    start_date       date,
    end_date         date,
    last_report_date date,
    owner_id         bigint,
    result           varchar(255)
);

create table reports
(
    id               bigserial primary key,
    photo_id         varchar(255),
    food_ration      varchar(255),
    general_health   varchar(255),
    behavior_changes varchar(255),
    receive_date     date,
    trial_period_id  bigint
);

create table cat
(
    id         bigserial not null primary key,
    name       varchar(255),
    age        integer,
    is_healthy boolean,
    vaccinated boolean,
    owner_id   bigint
);

create table dog
(
    id         bigserial not null primary key,
    name       varchar(255),
    age        integer,
    is_healthy boolean,
    vaccinated boolean,
    owner_id   bigint
);

create table users
(
    telegram_id bigint not null primary key,
    name        varchar(255),
    last_name   varchar(255),
    phone       varchar(255)
);

create table catowner
(
    telegram_id bigint not null primary key,
    name        varchar(255),
    last_name   varchar(255),
    phone       varchar(255)
);

create table dogowner
(
    telegram_id bigint not null primary key,
    name        varchar(255),
    last_name   varchar(255),
    phone       varchar(255)
);