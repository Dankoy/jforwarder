alter table chats add column date_created timestamp not null default (now() at time zone 'utc');
alter table chats add column date_modified timestamp;
