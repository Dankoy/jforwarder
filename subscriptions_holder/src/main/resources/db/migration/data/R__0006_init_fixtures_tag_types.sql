-- after 5.0 migration
insert into types(NAME)
values ('')
on conflict do nothing;