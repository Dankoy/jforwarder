-- clear all tables from production fixtures and insert test fixtures
delete
from community_section;
delete
from sections;
delete
from communities;
delete
from orders;
delete
from scopes;
delete
from types;
delete
from subscription_types;

-- make autoincrement start with 1 again

SELECT setval(pg_get_serial_sequence('sections', 'id'), coalesce(max(id), 0) + 1, false)
FROM sections;
SELECT setval(pg_get_serial_sequence('communities', 'id'), coalesce(max(id), 0) + 1, false)
FROM communities;
SELECT setval(pg_get_serial_sequence('orders', 'id'), coalesce(max(id), 0) + 1, false)
FROM orders;
SELECT setval(pg_get_serial_sequence('scopes', 'id'), coalesce(max(id), 0) + 1, false)
FROM scopes;
SELECT setval(pg_get_serial_sequence('types', 'id'), coalesce(max(id), 0) + 1, false)
FROM types;
SELECT setval(pg_get_serial_sequence('subscription_types', 'id'), coalesce(max(id), 0) + 1, false)
FROM subscription_types;

-- insert fixtures

insert into sections(NAME)
values ('daily'),
       ('weekly');

insert into communities(EXTERNAL_ID, NAME)
values ('75', 'cars'),
       ('19', 'memes')
;

insert into community_section(community_id, section_id)
values (1, 1),
       (1, 2),
       (2, 1),
       (2, 2)
;

-- orders

INSERT INTO orders(NAME, value)
SELECT 'likes_count', 'top'
WHERE NOT EXISTS (SELECT name, value FROM orders WHERE name = 'likes_count' and value = 'top');

INSERT INTO orders(NAME, value)
SELECT 'newest_popular', 'popular'
WHERE NOT EXISTS (SELECT name, value
                  FROM orders
                  WHERE name = 'newest_popular'
                    and value = 'popular');

-- scopes

insert into scopes(NAME)
values ('all')
on conflict do nothing;

--types

insert into types(NAME)
values ('')
on conflict do nothing;

-- update value for orders

update
    orders
set value = 'popular'
where id =
      (select id
       from orders
       where orders."name" = 'newest_popular');

update
    orders
set value = 'top'
where id =
      (select id
       from orders
       where orders."name" = 'likes_count');


insert into subscription_types(type)
values ('community'),
       ('tag'),
       ('channel');

-- update current for tag
update orders
set subscription_type_id = subscription_types.id
from subscription_types
where type = 'tag';

-- insert for channel
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