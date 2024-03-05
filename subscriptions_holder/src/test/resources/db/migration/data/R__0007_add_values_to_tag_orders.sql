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

update
    orders
set value = 'views_count'
where id =
      (select id
       from orders
       where orders."name" = 'views_count');

update
    orders
set value = 'fresh'
where id =
      (select id
       from orders
       where orders."name" = 'newest');