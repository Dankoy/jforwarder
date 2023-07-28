drop table if exists tag_subscriptions;

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
    last_permalink varchar(10)
);

ALTER TABLE tag_subscriptions
    ADD UNIQUE (tag_id, chat_id);
