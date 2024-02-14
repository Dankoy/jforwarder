-- applied after 5.0 migration after renaming
insert into orders(NAME)
values ('likes_count'),
       ('newest_popular'),
       ('views_count'),
       ('newest')
on conflict do nothing;