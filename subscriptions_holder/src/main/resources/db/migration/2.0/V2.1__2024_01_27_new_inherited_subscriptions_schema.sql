drop table if exists subscriptions;
drop table if exists tag_subs;
drop table if exists community_subs;

create table subscriptions
(
    id             bigserial not null
        constraint subscriptions_pkey
            primary key,
    chat_id        bigint references chats (id) on delete cascade,
    last_permalink varchar(10)
);

create table tag_subs
(
    id           bigserial not null
        constraint tag_subscriptions_pkey
            primary key
        constraint tag_subscriptions_subscriptions_id_fk
            references subscriptions,
    community_id bigint references communities (id) on delete cascade,
    section_id   bigint references sections (id)
);

create table community_subs
(
    id       bigserial not null
        constraint community_subscriptions_pkey
            primary key
        constraint community_subscriptions_subscriptions_id_fk
            references subscriptions,
    tag_id   bigint references tags (id) on delete cascade,
    order_id bigint references tag_orders (id),
    scope_id bigint references tag_scopes (id),
    type_id  bigint references tag_types (id)
);