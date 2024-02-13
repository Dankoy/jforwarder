drop table if exists subscription_types;

create table subscription_types
(
    id   bigserial primary key,
    type varchar(20) unique
);




