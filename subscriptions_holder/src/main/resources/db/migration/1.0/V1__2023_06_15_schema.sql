drop table if exists communities;
drop table if exists telegram_chats;
drop table if exists community_chat;
drop table if exists sections;


create table communities
(
    id          bigserial primary key,
    external_id bigint,
    name        varchar(255) not null,
--     last_permalink varchar(10),
    section_id  bigint       not null
);
create table telegram_chats
(
    id       bigserial primary key,
    chat_id  bigint unique not null,
    username varchar(50)
);
create table sections
(
    id   bigserial primary key,
    name varchar(20) unique not null
);

-- create table community_chat
-- (
--     community_id     bigint references communities (id) on delete cascade,
--     telegram_chat_id bigint references telegram_chats (id),
--     primary key (community_id, telegram_chat_id)
-- );


create table subscription_chat_permalink
(
    community_id     bigint references communities (id) on delete cascade,
    telegram_chat_id bigint references telegram_chats (id) on delete cascade,
    last_permalink   varchar(10)
);
