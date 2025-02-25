package ru.dankoy.telegramchatservice.core.repository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SortField;
import org.jooq.TableField;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ru.dankoy.telegramchatservice.core.domain.dto.ChatDTO;
import ru.dankoy.telegramchatservice.core.domain.jooq.tables.Chats;
import ru.dankoy.telegramchatservice.core.domain.jooq.tables.records.ChatsRecord;
import ru.dankoy.telegramchatservice.core.mapper.ChatMapper;
import ru.dankoy.telegramchatservice.core.specifications.telegramchat.criteria.SearchCriteria;
import ru.dankoy.telegramchatservice.core.specifications.telegramchat.filter.TelegramChatFilter;

import static org.jooq.impl.DSL.*;

import static ru.dankoy.telegramchatservice.core.domain.jooq.tables.Chats.*;

@RequiredArgsConstructor
@Component
public class TelegramChatDaoJooq implements TelegramChatDao {

    private final DSLContext dsl;
    private final ChatMapper chatMapper;

    // TODO: add specifications
    @Override
    public Page<ChatDTO> findAll(List<SearchCriteria> searchParams, Pageable pageable) {

        var offset = pageable.getOffset();
        var limit = pageable.getPageSize();

        List<ChatsRecord> records = dsl
                .select(Chats.CHATS.ID, Chats.CHATS.CHAT_ID, Chats.CHATS.TYPE,
                        Chats.CHATS.FIRST_NAME, Chats.CHATS.LAST_NAME, Chats.CHATS.USERNAME,
                        Chats.CHATS.MESSAGE_THREAD_ID,
                        Chats.CHATS.DATE_CREATED, Chats.CHATS.DATE_MODIFIED)
                .from(Chats.CHATS)
                .orderBy(getSortFields(pageable.getSort()))
                .offset(offset)
                .limit(limit)
                .fetchInto(CHATS);

        // TODO: should have condition too
        var total = dsl
                .select(count())
                .from(CHATS)
                .fetchOne().into(long.class);

        List<ChatDTO> chats = records.stream()
                .map(r -> chatMapper.toChatDTO(r))
                .toList();

        return new PageImpl<>(chats, pageable, total);
    }

    @Override
    public Optional<ChatDTO> findByChatId(long chatId) {

        var condition = noCondition();
        condition.and(CHATS.CHAT_ID.equal(chatId));

        ChatsRecord chatsRecord = dsl.fetchOne(CHATS, condition);

        var dto = chatMapper.toChatDTO(chatsRecord);

        return Optional.ofNullable(dto);

    }

    @Override
    public Optional<ChatDTO> findByChatIdAndMessageThreadId(long chatId, Integer threadId) {

        var condition = noCondition();
        condition.and(CHATS.CHAT_ID.equal(chatId))
                .and(CHATS.MESSAGE_THREAD_ID.eq(threadId));

        ChatsRecord chatsRecord = dsl.fetchOne(CHATS, condition);

        var dto = chatMapper.toChatDTO(chatsRecord);

        return Optional.ofNullable(dto);

    }

    @Override
    public Optional<ChatDTO> findForUpdateByChatIdAndMessageThreadId(long chatId, Integer threadId) {

        var condition = noCondition();
        condition.and(CHATS.CHAT_ID.equal(chatId))
                .and(CHATS.MESSAGE_THREAD_ID.eq(threadId));

        ChatsRecord chatsRecord = dsl.select()
                .from(CHATS)
                .where(condition)
                .forUpdate()
                .wait(3)
                .fetchOne().into(ChatsRecord.class);

        var dto = chatMapper.toChatDTO(chatsRecord);

        return Optional.ofNullable(dto);
    }

    @Override
    public Optional<ChatDTO> findForUpdateById(long id) {

        var condition = noCondition();
        condition.and(CHATS.ID.equal(id));

        ChatsRecord chatsRecord = dsl.select()
                .from(CHATS)
                .where(condition)
                .forUpdate()
                .wait(3)
                .fetchOne().into(ChatsRecord.class);

        var dto = chatMapper.toChatDTO(chatsRecord);

        return Optional.ofNullable(dto);

    }

    // uses reflection. Bad design.
    private Collection<SortField<?>> getSortFields(Sort sortSpecification) {
        Collection<SortField<?>> querySortFields = new ArrayList<>();

        if (sortSpecification == null) {
            return querySortFields;
        }

        Iterator<Sort.Order> specifiedFields = sortSpecification.iterator();

        while (specifiedFields.hasNext()) {
            Sort.Order specifiedField = specifiedFields.next();

            String sortFieldName = specifiedField.getProperty();
            Sort.Direction sortDirection = specifiedField.getDirection();

            TableField tableField = getTableField(sortFieldName);
            SortField<?> querySortField = convertTableFieldToSortField(tableField, sortDirection);
            querySortFields.add(querySortField);
        }

        return querySortFields;
    }

    private TableField getTableField(String sortFieldName) {
        TableField sortField = null;
        try {
            Field tableField = CHATS.getClass().getField(sortFieldName);
            sortField = (TableField) tableField.get(CHATS);
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            String errorMessage = String.format("Could not find table field: {}", sortFieldName);
            throw new InvalidDataAccessApiUsageException(errorMessage, ex);
        }

        return sortField;
    }

    private SortField<?> convertTableFieldToSortField(TableField tableField, Sort.Direction sortDirection) {
        if (sortDirection == Sort.Direction.ASC) {
            return tableField.asc();
        } else {
            return tableField.desc();
        }
    }

}
