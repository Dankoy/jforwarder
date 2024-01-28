-- Copy community subscriptions
-- INSERT INTO subscriptions (chat_id,
--                            last_permalink)
-- SELECT chat_id,
--        last_permalink
-- FROM community_subscriptions;
--
-- INSERT INTO community_subs (section_id,
--                            community_id)
-- SELECT chat_id,
--        last_permalink
-- FROM community_subscriptions;

-- Copy community subscriptions
with full_select as (select *
                     from community_subscriptions),
     first_insert as (
         insert into subscriptions (chat_id, last_permalink)
             SELECT full_select.chat_id,
                    full_select.last_permalink
             FROM full_select
             RETURNING id)
insert
into community_subs (id, section_id, community_id)
SELECT first_insert.id,
       full_select.section_id,
       full_select.community_id
FROM full_select,
     first_insert;

-- Copy tag subscriptions
with full_select as (select *
                     from tag_subscriptions),
     first_insert as (
         insert into subscriptions (chat_id, last_permalink)
             SELECT full_select.chat_id,
                    full_select.last_permalink
             FROM full_select
             RETURNING id)
insert
into tag_subs (id, tag_id, order_id, scope_id, type_id)
SELECT first_insert.id,
       full_select.tag_id,
       full_select.order_id,
       full_select.scope_id,
       full_select.type_id
FROM full_select,
     first_insert;