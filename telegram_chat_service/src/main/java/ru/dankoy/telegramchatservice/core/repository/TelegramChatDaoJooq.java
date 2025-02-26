package ru.dankoy.telegramchatservice.core.repository;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.RecordMapper;
import org.jooq.Records;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SortField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.impl.DSL;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.dankoy.telegramchatservice.core.domain.dto.ChatDTO;
import ru.dankoy.telegramchatservice.core.domain.jooq.tables.Chats;
import ru.dankoy.telegramchatservice.core.domain.jooq.tables.records.ChatsRecord;
import ru.dankoy.telegramchatservice.core.mapper.ChatMapper;
import ru.dankoy.telegramchatservice.core.service.condition.SearchQueryConditionConsumer;
import ru.dankoy.telegramchatservice.core.service.jooqfieldparser.JooqFieldParser;
import ru.dankoy.telegramchatservice.core.service.specifications.telegramchat.criteria.SearchCriteria;
import ru.dankoy.telegramchatservice.core.service.specifications.telegramchat.filter.TelegramChatFilter;

import static org.jooq.impl.DSL.*;

import static ru.dankoy.telegramchatservice.core.domain.jooq.tables.Chats.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class TelegramChatDaoJooq implements TelegramChatDao {

    private final DSLContext dsl;
    private final ChatMapper chatMapper;
    private final JooqFieldParser parser;

    @Override
    public Page<ChatDTO> findAll(List<SearchCriteria> searchParams, Pageable pageable) {

        var offset = pageable.getOffset();
        var limit = pageable.getPageSize();

        var condition = noCondition();

        Table<ChatsRecord> table = Chats.CHATS.asTable();

        SearchQueryConditionConsumer<ChatsRecord> conditionConsumer = new SearchQueryConditionConsumer<>(condition,
                parser, table);

        searchParams.forEach(conditionConsumer::accept);

        condition = conditionConsumer.getCondition();

        var sort = parser.getSortFields(table, pageable.getSort());

        List<ChatsRecord> records = dsl
                .select(Chats.CHATS.ID, Chats.CHATS.CHAT_ID, Chats.CHATS.TYPE,
                        Chats.CHATS.FIRST_NAME, Chats.CHATS.LAST_NAME, Chats.CHATS.USERNAME,
                        Chats.CHATS.MESSAGE_THREAD_ID,
                        Chats.CHATS.DATE_CREATED, Chats.CHATS.DATE_MODIFIED)
                .from(Chats.CHATS)
                .where(condition)
                .orderBy(sort)
                .offset(offset)
                .limit(limit)
                .fetchInto(CHATS);

        // TODO: should have condition too
        var total = dsl
                .select(count())
                .from(CHATS)
                .where(condition)
                .fetchOne().into(long.class);

        List<ChatDTO> chats = records.stream()
                .map(r -> chatMapper.toChatDTO(r))
                .toList();

        return new PageImpl<>(chats, pageable, total);
    }

    @Override
    public Optional<ChatDTO> findByChatId(long chatId) {

        var condition = noCondition();
        condition = condition.and(CHATS.CHAT_ID.equal(chatId));

        ChatsRecord chatsRecord = dsl.fetchOne(CHATS, condition);

        var dto = chatMapper.toChatDTO(chatsRecord);

        return Optional.ofNullable(dto);

    }

    @Override
    public Optional<ChatDTO> findByChatIdAndMessageThreadId(long chatId, Integer threadId) {

        var condition = noCondition();
        condition = condition.and(CHATS.CHAT_ID.eq(chatId))
                .and(CHATS.MESSAGE_THREAD_ID.eq(threadId));

        ChatsRecord chatsRecord = dsl.fetchOne(CHATS, condition);

        var dto = chatMapper.toChatDTO(chatsRecord);

        return Optional.ofNullable(dto);

    }

    @Override
    public Optional<ChatDTO> findForUpdateByChatIdAndMessageThreadId(long chatId, Integer threadId) {

        var condition = noCondition();
        condition = condition.and(CHATS.CHAT_ID.eq(chatId))
                .and(CHATS.MESSAGE_THREAD_ID.eq(threadId));

        Optional<Record> chatsRecord = dsl.select()
                .from(CHATS)
                .where(condition)
                .forUpdate()
                .wait(3)
                .fetchOptional();

        if (chatsRecord.isPresent()) {
            var rec = chatsRecord.get();
            var res = rec.into(ChatsRecord.class);
            var dto = chatMapper.toChatDTO(res);
            return Optional.of(dto);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<ChatDTO> findForUpdateById(long id) {

        var condition = noCondition();
        condition = condition.and(CHATS.ID.eq(id));

        Optional<Record> chatsRecord = dsl.select()
                .from(CHATS)
                .where(condition)
                .forUpdate()
                .wait(3)
                .fetchOptional();

        return chatsRecord.map(rec -> chatMapper.toChatDTO(rec.into(ChatsRecord.class)));

    }

    @Override
    public ChatDTO save(ChatDTO chat) {

        var jooqPojo = chatMapper.toJooqPojo(chat);

        // same as below
        var chatsRecord = new ChatsRecord(jooqPojo);
        chatsRecord.setDateCreated(LocalDateTime.now(ZoneOffset.UTC));
        // chatsRecord.store();

        var result = dsl.insertInto(CHATS)
                .set(chatsRecord)
                .returning()
                .fetchOne().into(ChatsRecord.class);

        return chatMapper.toChatDTO(result);

        // store gets identiy and pass it in record.
        // var record = dsl.newRecord(CHATS, jooqPojo);
        // record.set(CHATS.DATE_CREATED, LocalDateTime.now(ZoneOffset.UTC));
        // record.store();

    }

    @Override
    public ChatDTO update(ChatDTO chat) {

        var condition = noCondition();
        condition = condition.and(CHATS.ID.eq(chat.getId()));

        var jooqPojo = chatMapper.toJooqPojo(chat);
        jooqPojo.setId(null);

        // same as below
        var chatsRecord = new ChatsRecord(jooqPojo);
        chatsRecord.setDateModified(LocalDateTime.now(ZoneOffset.UTC));

        var result = dsl.update(CHATS)
                .set(chatsRecord)
                .where(condition)
                .returning()
                .fetchOne()
                .into(ChatsRecord.class);

        return chatMapper.toChatDTO(result);

    }

    @Override
    public List<ChatDTO> saveAll(List<ChatDTO> chats) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAll'");
    }

    @Override
    public void deleteBatch(List<ChatDTO> chats) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteBatch'");
    }

}
