drop table if exists channels;

create table channels
(
    id        bigserial primary key,
    title     varchar(50) unique,
    permalink varchar(50) unique
);

create table channel_subs
(
    id         bigserial not null
        constraint channel_subscriptions_inherited_pkey
            primary key
        constraint channel_subscriptions_inherited_subscriptions_id_fk
            references subscriptions,
    channel_id bigint references channels (id) on delete cascade,
    order_id   bigint references orders (id),
    scope_id   bigint references scopes (id),
    type_id    bigint references types (id)
);



