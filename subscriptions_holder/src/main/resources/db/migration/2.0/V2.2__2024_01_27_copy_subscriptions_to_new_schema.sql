-- Copy community subscriptions
do $$
    declare
        cs record;
        new_id integer;
    begin
        for cs in select id, community_id, section_id, chat_id, last_permalink from community_subscriptions cc
            loop
                insert into subscriptions (chat_id, last_permalink) values (cs.chat_id, cs.last_permalink) returning id into new_id;
                insert into community_subs (id, community_id, section_id) values (new_id, cs.community_id, cs.section_id);
            end loop;
    end;
$$;


-- Copy tag subscriptions
do $$
    declare
        ts record;
        new_id integer;
    begin
        for ts in select tag_id, chat_id, order_id, scope_id, type_id, last_permalink from tag_subscriptions
            loop
                insert into subscriptions (chat_id, last_permalink) values (ts.chat_id, ts.last_permalink) returning id into new_id;
                insert into tag_subs (id, tag_id, order_id, scope_id, type_id) values (new_id, ts.tag_id, ts.order_id, ts.scope_id, ts.type_id);
            end loop;
    end;
$$;