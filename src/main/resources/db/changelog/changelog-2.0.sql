--liquibase formatted sql

--changeset catunderglue:1

alter table trial_periods
    add animal_type varchar(255);

alter table trial_periods
    add animal_id bigint;

--changeset profmi2022:3
alter table users
    add shelter_type varchar(255);

alter table users
    add shelter_name varchar(255);
