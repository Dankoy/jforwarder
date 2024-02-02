alter table tag_orders
    add value varchar(15);

update
    tag_orders
set value = 'popular'
where id =
      (select id
       from tag_orders
       where tag_orders."name" = 'newest_popular');

update
    tag_orders
set value = 'top'
where id =
      (select id
       from tag_orders
       where tag_orders."name" = 'likes_count');

update
    tag_orders
set value = 'views_count'
where id =
      (select id
       from tag_orders
       where tag_orders."name" = 'views_count');

update
    tag_orders
set value = 'fresh'
where id =
      (select id
       from tag_orders
       where tag_orders."name" = 'newest');



