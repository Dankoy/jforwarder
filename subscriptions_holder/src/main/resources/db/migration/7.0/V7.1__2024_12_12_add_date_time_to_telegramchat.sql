alter table chats add column date_created timestamp not null default current_timestamp;
alter table chats add column date_disabled timestamp;
