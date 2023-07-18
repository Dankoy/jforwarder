drop table if exists communities;
drop table if exists telegram_chats;
drop table if exists community_chat;
drop table if exists sections;
drop table if exists community_section;


create table communities
(
    id          bigserial primary key,
    external_id bigint,
    name        varchar(255) not null
);

create table chats
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

create table community_section
(
    community_id bigint references communities (id) on delete cascade,
    section_id   bigint references sections (id),
    primary key (community_id, section_id)
);


create table subscriptions
(
    id   bigserial primary key,
    community_id     bigint references communities (id) on delete cascade,
    section_id       bigint references sections (id),
    chat_id bigint references chats (id) on delete cascade,
    last_permalink   varchar(10)
);
