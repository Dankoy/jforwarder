-- Active tables 
alter table subscriptions alter column last_permalink varchar(30);
alter table sent_coubs_registry alter column coub_permalink varchar(30) not null;

-- Not active tables, for removal
alter table community_subscriptions alter column last_permalink varchar(30);
alter table tag_subscriptions alter column last_permalink varchar(30);
