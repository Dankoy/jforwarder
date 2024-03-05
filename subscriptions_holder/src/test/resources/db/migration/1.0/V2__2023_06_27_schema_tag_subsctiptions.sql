drop table if exists tag_subscriptions;
drop table if exists tags;


create table tag_orders
(
    id   bigserial primary key,
    name varchar(20) unique
);

create table tag_scopes
(
    id   bigserial primary key,
    name varchar(20) unique
);

create table tag_types
(
    id   bigserial primary key,
    name varchar(20) unique
);

create table tags
(
    id    bigserial primary key,
    title varchar(50) unique
);

create table tag_subscriptions
(
    id             bigserial primary key,
    tag_id         bigint references tags (id) on delete cascade,
    chat_id        bigint references chats (id) on delete cascade,
    order_id       bigint references tag_orders (id),
    scope_id       bigint references tag_scopes (id),
    type_id        bigint references tag_types (id),
    last_permalink varchar(10)
);

ALTER TABLE tag_subscriptions
    ADD UNIQUE (tag_id, chat_id, order_id);
