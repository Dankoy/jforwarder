alter table subscriptions add column created_at timestamp not null default now();
alter table subscriptions add column modified_at timestamp default now();
