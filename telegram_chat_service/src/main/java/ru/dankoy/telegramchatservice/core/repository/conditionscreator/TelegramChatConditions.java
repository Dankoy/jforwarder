package ru.dankoy.telegramchatservice.core.repository.conditionscreator;

import static org.jooq.impl.DSL.noCondition;
import static ru.dankoy.telegramchatservice.core.domain.jooq.tables.Chats.CHATS;

import org.jooq.Condition;
import ru.dankoy.telegramchatservice.core.domain.filter.TelegramChatFilter;

public class TelegramChatConditions {

  private TelegramChatConditions() {
    // Private constructor to prevent instantiation
  }

  public static Condition filterBy(TelegramChatFilter chatFilter) {

    var condition = noCondition();

    if (chatFilter.chatId() != null)
      condition = condition.and(CHATS.CHAT_ID.eq(chatFilter.chatId()));
    if (chatFilter.id() != null) condition = condition.and(CHATS.ID.eq(chatFilter.id()));
    if (chatFilter.messageThreadId() != null)
      condition = condition.and(CHATS.MESSAGE_THREAD_ID.eq(chatFilter.messageThreadId()));
    if (chatFilter.type() != null) condition = condition.and(CHATS.TYPE.like(chatFilter.type()));
    if (chatFilter.lastName() != null)
      condition = condition.and(CHATS.LAST_NAME.like(chatFilter.lastName()));
    if (chatFilter.firstName() != null)
      condition = condition.and(CHATS.FIRST_NAME.like(chatFilter.firstName()));
    if (chatFilter.username() != null)
      condition = condition.and(CHATS.USERNAME.like(chatFilter.username()));
    if (chatFilter.title() != null) condition = condition.and(CHATS.TITLE.like(chatFilter.title()));
    if (chatFilter.active() != null)
      condition = condition.and(CHATS.ACTIVE.eq(chatFilter.active()));

    return condition;
  }
}
