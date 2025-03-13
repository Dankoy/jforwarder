package ru.dankoy.telegramchatservice.core.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jooq.impl.DSL.*;
import static ru.dankoy.telegramchatservice.core.domain.jooq.tables.Chats.CHATS;

import java.util.Optional;
import java.util.UUID;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.springframework.context.annotation.Import;
import ru.dankoy.telegramchatservice.core.component.jooqfieldparser.JooqFieldParserImpl;
import ru.dankoy.telegramchatservice.core.domain.jooq.tables.Chats;
import ru.dankoy.telegramchatservice.core.domain.jooq.tables.records.ChatsRecord;
import ru.dankoy.telegramchatservice.core.mapper.ChatMapper;
import ru.dankoy.telegramchatservice.core.mapper.ChatMapperImpl;
import ru.dankoy.telegramchatservice.core.service.ChatMaker;
import ru.dankoy.telegramchatservice.core.service.TestContainerBase;

@JooqTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({TelegramChatDaoJooq.class, ChatMapperImpl.class, JooqFieldParserImpl.class})
class TelegramChatDaoJooqTest extends TestContainerBase implements ChatMaker {

  @Autowired private DSLContext dsl;

  @Autowired private ChatMapper chatMapper;

  @Autowired private TelegramChatDaoJooq telegramChatDao;

  private static final UUID uuid = UUID.randomUUID();

  @Test
  void findByIdTest() {

    var chat = makeChat(uuid, 1);

    var jooqPojo = chatMapper.toJooqPojo(chat);

    var chatsRecord = new ChatsRecord(jooqPojo);
    var result =
        dsl.insertInto(Chats.CHATS).set(chatsRecord).returning().fetchOne().into(ChatsRecord.class);

    var id = result.get("id");

    var actual = telegramChatDao.findForUpdateById(UUID.fromString(id.toString()));

    assertThat(actual.get()).isNotNull().isEqualTo(chat);
  }

  @Test
  void findByChatIdTest() {

    var chat = makeChat(uuid, 1);

    var jooqPojo = chatMapper.toJooqPojo(chat);

    var chatsRecord = new ChatsRecord(jooqPojo);

    dsl.insertInto(Chats.CHATS).set(chatsRecord).returning().fetchOne().into(ChatsRecord.class);

    var actual = telegramChatDao.findByChatId(chat.getChatId());

    assertThat(actual.get()).isNotNull().isEqualTo(chat);
  }

  @Test
  void findByChatIdTest_ExpectsNull() {

    var chat = makeChat(uuid, 1);

    var actual = telegramChatDao.findByChatId(chat.getChatId());

    assertThat(actual).isEmpty();
  }

  @Test
  void findByChatIdAndMessageThreadIdTest() {

    var chat = makeChat(uuid, 1);

    var jooqPojo = chatMapper.toJooqPojo(chat);

    var chatsRecord = new ChatsRecord(jooqPojo);

    dsl.insertInto(Chats.CHATS).set(chatsRecord).returning().fetchOne().into(ChatsRecord.class);

    var actual =
        telegramChatDao.findByChatIdAndMessageThreadId(chat.getChatId(), chat.getMessageThreadId());

    assertThat(actual.get()).isNotNull().isEqualTo(chat);
  }

  @Test
  void findByChatIdAndMessageThreadIdTest_ExpectsNull() {

    var chat = makeChat(uuid, 1);

    var actual =
        telegramChatDao.findByChatIdAndMessageThreadId(chat.getChatId(), chat.getMessageThreadId());

    assertThat(actual).isEmpty();
  }

  @Test
  void saveTest() {

    var chat = makeChat(uuid, 1);

    var saved = telegramChatDao.save(chat);

    var condition = noCondition();
    condition = condition.and(CHATS.ID.eq(saved.getId()));

    Optional<Record> chatsRecord = dsl.select().from(Chats.CHATS).where(condition).fetchOptional();

    var actual = chatsRecord.map(rec -> chatMapper.toChatDTO(rec.into(ChatsRecord.class)));

    assertThat(saved).isEqualTo(actual.get());
  }

  @Test
  void updateTest() {

    var chat = makeChat(uuid, 1);

    var jooqPojo = chatMapper.toJooqPojo(chat);

    var chatsRecord = new ChatsRecord(jooqPojo);

    dsl.insertInto(Chats.CHATS).set(chatsRecord).returning().fetchOne().into(ChatsRecord.class);

    chat.setFirstName("sdfsdgfhg");

    var updated = telegramChatDao.update(chat);

    var condition = noCondition();
    condition = condition.and(CHATS.ID.eq(updated.getId()));

    Optional<Record> r = dsl.select().from(Chats.CHATS).where(condition).fetchOptional();

    var actual = r.map(rec -> chatMapper.toChatDTO(rec.into(ChatsRecord.class)));

    assertThat(updated).isEqualTo(actual.get());
  }
}
