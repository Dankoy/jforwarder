package ru.dankoy.telegramchatservice.core.component.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.dankoy.telegramchatservice.core.domain.Chat;
import ru.dankoy.telegramchatservice.core.domain.filter.TelegramChatFilter;

public class TelegramChatSpecification {

  private static final String CHAT_ID = "chat_id";
  private static final String TYPE = "type";
  private static final String TITLE = "title";
  private static final String FIRST_NAME = "firstName";
  private static final String LAST_NAME = "lastName";
  private static final String USERNAME = "username";
  private static final String ACTIVE = "active";
  private static final String MESSAGE_THREAD_ID = "message_thread_id";

  private TelegramChatSpecification() {
    // Private constructor to prevent instantiation
  }

  public static Specification<Chat> filterBy(TelegramChatFilter chatFilter) {
    return Specification.where(hasChatId(chatFilter.chatId()))
        .and(hasType(chatFilter.type()))
        .and(hasTitle(chatFilter.title()))
        .and(hasMessageThreadId(chatFilter.messageThreadId()))
        .and(isActive(chatFilter.active()))
        .and(hasFirstName(chatFilter.firstName()))
        .and(hasLastName(chatFilter.lastName()))
        .and(hasUserName(chatFilter.username()));
  }

  private static Specification<Chat> hasChatId(Long chatId) {
    return ((root, query, cb) ->
        chatId == null ? cb.conjunction() : cb.equal(root.get(CHAT_ID), chatId));
  }

  private static Specification<Chat> hasMessageThreadId(Long messageThreadId) {
    return ((root, query, cb) ->
        messageThreadId == null
            ? cb.conjunction()
            : cb.equal(root.get(MESSAGE_THREAD_ID), messageThreadId));
  }

  private static Specification<Chat> hasType(String type) {
    return ((root, query, cb) ->
        type == null || type.isEmpty() ? cb.conjunction() : cb.equal(root.get(TYPE), type));
  }

  private static Specification<Chat> hasTitle(String title) {
    return ((root, query, cb) ->
        title == null || title.isEmpty() ? cb.conjunction() : cb.equal(root.get(TITLE), title));
  }

  private static Specification<Chat> hasFirstName(String firstName) {
    return ((root, query, cb) ->
        firstName == null || firstName.isEmpty()
            ? cb.conjunction()
            : cb.equal(root.get(FIRST_NAME), firstName));
  }

  private static Specification<Chat> hasLastName(String lastName) {
    return ((root, query, cb) ->
        lastName == null || lastName.isEmpty()
            ? cb.conjunction()
            : cb.equal(root.get(LAST_NAME), lastName));
  }

  private static Specification<Chat> hasUserName(String userName) {
    return ((root, query, cb) ->
        userName == null || userName.isEmpty()
            ? cb.conjunction()
            : cb.equal(root.get(USERNAME), userName));
  }

  private static Specification<Chat> isActive(Boolean active) {
    return ((root, query, cb) -> {
      if (active == null) return cb.conjunction();
      else if (active == Boolean.FALSE) return cb.isFalse(root.get(ACTIVE));
      else return cb.isTrue(root.get(ACTIVE));
    });
  }
}
