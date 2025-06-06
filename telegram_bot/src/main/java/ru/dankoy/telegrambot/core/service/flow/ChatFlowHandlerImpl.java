package ru.dankoy.telegrambot.core.service.flow;

import feign.FeignException.NotFound;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import ru.dankoy.telegrambot.core.exceptions.BotFlowException;
import ru.dankoy.telegrambot.core.service.chat.TelegramChatService;
import ru.dankoy.telegrambot.core.service.localeprovider.LocaleProvider;
import ru.dankoy.telegrambot.core.service.localization.LocalisationService;

@Slf4j
@RequiredArgsConstructor
@Component
public class ChatFlowHandlerImpl implements ChatFlowHandler {

  private final TelegramChatService telegramChatService;

  private final LocaleProvider localeProvider;

  private final LocalisationService localisationService;

  @Override
  public Message checkChatStatus(org.springframework.messaging.Message<Message> message) {

    var tChat = message.getPayload().getChat();
    var messageThreadId = message.getPayload().getMessageThreadId();

    log.info("Check chat status - {}", tChat.getId());
    try {
      var found = telegramChatService.getChatByIdAndMessageThreadId(tChat.getId(), messageThreadId);
      log.info("Found chat - {}-{}", tChat.getId(), messageThreadId);

      if (!found.isActive()) {
        //        sendMessage.setText(
        //            localisationService.getLocalizedMessage(
        //                "chatNotActive", null, localeProvider.getLocale(message)));
        //        send(sendMessage);

        throw new BotFlowException(
            "Chat is not active", message.getPayload(), "chatNotActive", null);
      }

    } catch (NotFound e) {
      log.warn("Chat not found - {}", tChat.getId());
      //      sendMessage.setText(
      //          localisationService.getLocalizedMessage(
      //              "chatNotFound", null, localeProvider.getLocale(message)));
      //      send(sendMessage);
      throw new BotFlowException(
          "Accessed subscribe command without start", message.getPayload(), "chatNotFound", null);
    }
    return message.getPayload();
  }

  @Override
  public Message createChat(Message message) {
    return null;
  }
}
