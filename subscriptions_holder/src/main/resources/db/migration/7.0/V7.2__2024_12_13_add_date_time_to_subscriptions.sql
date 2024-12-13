alter table subscriptions add column created_at timestamp not null default (now() at time zone 'utc');
alter table subscriptions add column modified_at timestamp;
