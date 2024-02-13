-- update current
update orders
set subscription_type_id = subscription_types.id
from subscription_types
where type = 'tag';

-- -- insert for channel
with select_order_type as (select id
                           from subscription_types
                           where type = 'channel')
insert
into orders (name,
             value,
             subscription_type_id)
select 'likes_count',
       'most_liked',
       select_order_type.id
from select_order_type;

with select_order_type as (select id from subscription_types where type = 'channel')
insert
into orders(name, value, subscription_type_id)
select 'newest', 'most_recent', select_order_type.id
from select_order_type;

with select_order_type as (select id from subscription_types where type = 'channel')
insert
into orders(name, value, subscription_type_id)
select 'views_count', 'most_viewed', select_order_type.id
from select_order_type;

with select_order_type as (select id from subscription_types where type = 'channel')
insert
into orders(name, value, subscription_type_id)
select 'oldest', 'oldest', select_order_type.id
from select_order_type;