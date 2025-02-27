package ru.dankoy.telegramchatservice.core.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DisplayName("Test TelegramChatServiceImpl ")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({TelegramChatServiceImpl.class})
class TelegramChatServiceImplTest extends TestContainerBase implements ChatMaker {

  @PersistenceContext private EntityManager entityManager;

  @Autowired private TelegramChatServiceImpl telegramChatService;

  private static final long chatId = 123L;

  // @DisplayName("save expects correct response")
  // @Test
  // void saveTest_expectsCorrectResponse() {

  //   var toSave = makeChat(chatId);

  //   var actual = telegramChatService.save(toSave);

  //   var expected = entityManager.find(Chat.class, actual.getId());

  //   assertThat(actual).isEqualTo(expected);
  // }

  // @DisplayName("save expects correct response")
  // @Test
  // @Disabled(value = "because of topics support. chats.chat_id is not unique anymore.")
  // void saveTest_existingChat_expectsIntegrityError() {

  //   var toSave = makeChat(chatId);
  //   var toSave2 = makeChat(chatId);

  //   entityManager.persist(toSave);
  //   entityManager.flush();

  //   assertThatThrownBy(() -> telegramChatService.save(toSave2))
  //       .isInstanceOf(DataIntegrityViolationException.class);
  // }

  // @DisplayName("getByTelegramChatId expects correct response")
  // @Test
  // void getByTelegramChatIdTest_expectsCorrectResponse() {

  //   var toSave = makeChat(chatId);

  //   entityManager.persist(toSave);
  //   entityManager.flush();

  //   var actual = telegramChatService.getByTelegramChatId(chatId);

  //   assertThat(actual).hasValue(toSave);
  // }

  // @DisplayName("getByTelegramChatId expects correct response")
  // @Test
  // void getByTelegramChatIdTest_expectsEmptyOptionalCorrectResponse() {

  //   var actual = telegramChatService.getByTelegramChatId(chatId);

  //   assertThat(actual).isEmpty();
  // }
}
