package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.Chat;

@DisplayName("Test TelegramChatServiceImpl ")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({TelegramChatServiceImpl.class})
class TelegramChatServiceImplTest extends TestContainerBase {

  @PersistenceContext private EntityManager entityManager;

  @Autowired private TelegramChatServiceImpl telegramChatService;

  @DisplayName("save expects correct response")
  @Test
  void saveTest_expectsCorrectResponse() {

    var toSave = new Chat(0L, 123, "type", "title", "firstName", "lastName", "username", true);

    var actual = telegramChatService.save(toSave);

    var expected = entityManager.find(Chat.class, actual.getId());

    assertThat(actual).isEqualTo(expected);
  }

  @DisplayName("save expects correct response")
  @Test
  void saveTest_existingChat_expectsIntegrityError() {

    var toSave = new Chat(0L, 123, "type", "title", "firstName", "lastName", "username", true);
    var toSave2 = new Chat(0L, 123, "type", "title", "firstName", "lastName", "username", true);

    entityManager.persist(toSave);
    entityManager.flush();

    assertThatThrownBy(() -> telegramChatService.save(toSave2))
        .isInstanceOf(DataIntegrityViolationException.class);
  }

  @DisplayName("getByTelegramChatId expects correct response")
  @Test
  void getByTelegramChatIdTest_expectsCorrectResponse() {

    var chatId = 123L;

    var toSave = new Chat(0L, chatId, "type", "title", "firstName", "lastName", "username", true);

    entityManager.persist(toSave);
    entityManager.flush();

    var actual = telegramChatService.getByTelegramChatId(chatId);

    assertThat(actual).hasValue(toSave);
  }

  @DisplayName("getByTelegramChatId expects correct response")
  @Test
  void getByTelegramChatIdTest_expectsEmptyOptionalCorrectResponse() {

    var chatId = 123L;

    var actual = telegramChatService.getByTelegramChatId(chatId);

    assertThat(actual).isEmpty();
  }
}
