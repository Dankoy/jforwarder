package ru.dankoy.tcoubsinitiator.core.httpservice.argumentresolver;

import java.util.Objects;
import org.springframework.core.MethodParameter;
import org.springframework.web.service.invoker.HttpRequestValues;
import org.springframework.web.service.invoker.HttpServiceArgumentResolver;
import ru.dankoy.tcoubsinitiator.core.domain.telegramchatservice.filter.TelegramChatFilter;

public class TelegramChatFilterArgumentResolver implements HttpServiceArgumentResolver {

  @Override
  public boolean resolve(
      Object argument, MethodParameter parameter, HttpRequestValues.Builder requestValues) {

    if (!parameter.getParameterType().equals(TelegramChatFilter.class)) {
      return false;
    }

    if (argument != null) {
      var filter = (TelegramChatFilter) argument;

      var id = filter.getId();
      var chatId = filter.getChatId();
      var type = filter.getType();
      var title = filter.getTitle();
      var firstName = filter.getFirstName();
      var lastName = filter.getLastName();
      var username = filter.getUsername();
      var active = filter.getActive();
      var messageThreadId = filter.getMessageThreadId();

      if (Objects.nonNull(id)) {
        requestValues.addRequestParameter("id", String.valueOf(id));
      }
      if (Objects.nonNull(chatId)) {
        requestValues.addRequestParameter("chatId", String.valueOf(chatId));
      }
      if (Objects.nonNull(type)) {
        requestValues.addRequestParameter("type", String.valueOf(type));
      }
      if (Objects.nonNull(title)) {
        requestValues.addRequestParameter("title", String.valueOf(title));
      }
      if (Objects.nonNull(firstName)) {
        requestValues.addRequestParameter("firstName", String.valueOf(firstName));
      }
      if (Objects.nonNull(lastName)) {
        requestValues.addRequestParameter("lastName", String.valueOf(lastName));
      }
      if (Objects.nonNull(username)) {
        requestValues.addRequestParameter("username", String.valueOf(username));
      }
      if (Objects.nonNull(active)) {
        requestValues.addRequestParameter("active", String.valueOf(active));
      }
      if (Objects.nonNull(messageThreadId)) {
        requestValues.addRequestParameter("messageThreadId", String.valueOf(messageThreadId));
      }
    }

    return true;
  }
}
