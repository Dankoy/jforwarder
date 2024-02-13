-- insert for tag
with select_order_type as (select id from subscription_types where type = 'tag'),
     existing_order
         as (select * from orders where id = 1)
insert
into order_subscription_type(order_id, subscription_type_id)
values (existing_order.id, select_order_type.id);

with select_order_type as (select id from subscription_types where type = 'tag'),
     existing_order
         as (select * from orders where id = 2)
insert
into order_subscription_type(order_id, subscription_type_id)
values (existing_order.id, select_order_type.id);

with select_order_type as (select id from subscription_types where type = 'tag'),
     existing_order
         as (select * from orders where id = 3)
insert
into order_subscription_type(order_id, subscription_type_id)
values (existing_order.id, select_order_type.id);

with select_order_type as (select id from subscription_types where type = 'tag'),
     existing_order
         as (select * from orders where id = 4)
insert
into order_subscription_type(order_id, subscription_type_id)
values (existing_order.id, select_order_type.id);


-- insert for channel
with select_order_type as (select id from subscription_types where type = 'channel'),
     new_order
         as (insert into orders (name, value) values ('likes_count', 'most_liked') returning id)
insert
into order_subscription_type(order_id, subscription_type_id)
values (new_order.id, select_order_type.id);

with select_order_type as (select id from subscription_types where type = 'channel'),
     new_order
         as (insert into orders (name, value) values ('newest', 'most_recent') returning id)
insert
into order_subscription_type(order_id, subscription_type_id)
values (new_order.id, select_order_type.id);

with select_order_type as (select id from subscription_types where type = 'channel'),
     new_order
         as (insert into orders (name, value) values ('views_count', 'most_viewed') returning id)
insert
into order_subscription_type(order_id, subscription_type_id)
values (new_order.id, select_order_type.id);

with select_order_type as (select id from subscription_types where type = 'channel'),
     new_order
         as (insert into orders (name, value) values ('oldest', 'oldest') returning id)
insert
into order_subscription_type(order_id, subscription_type_id)
values (new_order.id, select_order_type.id);

