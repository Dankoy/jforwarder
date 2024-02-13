drop table if exists subscription_types;

create table subscription_types
(
    id   bigserial primary key,
    type varchar(20) unique
);

create table order_subscription_type
(
    order_id             bigint references orders (id) on delete cascade,
    subscription_type_id bigint references subscription_types (id),
    primary key (order_id, subscription_type_id)
);



