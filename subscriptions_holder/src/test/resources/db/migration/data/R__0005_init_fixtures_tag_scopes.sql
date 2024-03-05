-- after 5.0 migration
insert into scopes(NAME)
values ('all')
on conflict do nothing;