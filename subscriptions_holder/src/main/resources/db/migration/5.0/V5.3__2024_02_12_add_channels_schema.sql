drop table if exists channels;

create table channels
(
    id        bigserial primary key,
    title     varchar(50) unique,
    permalink varchar(50) unique
);

create table channel_subs
(
    id       bigserial not null
        constraint tag_subscriptions_inherited_pkey
            primary key
        constraint tag_subscriptions_inherited_subscriptions_id_fk
            references subscriptions,
    tag_id   bigint references tags (id) on delete cascade,
    order_id bigint references tag_orders (id),
    scope_id bigint references tag_scopes (id),
    type_id  bigint references tag_types (id)
);



