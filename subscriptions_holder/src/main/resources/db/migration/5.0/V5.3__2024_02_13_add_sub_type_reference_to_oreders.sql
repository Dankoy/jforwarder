alter table orders
    add subscription_type_id bigint references subscription_types (id);




