drop table if exists order_types;

create table order_types
(
    id   bigserial primary key,
    type varchar(20) unique
);

alter table tag_orders
    rename to orders;

alter table orders
    add type_id bigint references order_types (id);




