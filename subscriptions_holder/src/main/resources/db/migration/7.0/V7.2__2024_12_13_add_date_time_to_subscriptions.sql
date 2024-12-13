alter table subscriptions add column created_at timestamp not null default now();
