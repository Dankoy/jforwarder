alter table chats add column message_thread_id int;
alter table chats drop constraint "chats_chat_id_key";
alter table chats alter column chat_id set not null;




