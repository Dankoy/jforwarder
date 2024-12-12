alter table chats add column date_created timestamp not null default now();
alter table chats add column date_modified timestamp;
