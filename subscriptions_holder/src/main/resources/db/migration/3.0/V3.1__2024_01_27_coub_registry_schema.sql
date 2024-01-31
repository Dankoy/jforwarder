drop table if exists sent_coubs_registry;

create table sent_coubs_registry
(
    id              bigserial primary key,
    subscription_id bigint references subscriptions (id) on delete cascade,
    coub_permalink  varchar(10) not null,
    date_time       timestamp   not null default current_timestamp
);

