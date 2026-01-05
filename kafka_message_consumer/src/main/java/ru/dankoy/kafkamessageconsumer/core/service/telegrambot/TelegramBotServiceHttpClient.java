package ru.dankoy.kafkamessageconsumer.core.service.telegrambot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.kafkamessageconsumer.core.domain.message.CoubMessage;
import ru.dankoy.kafkamessageconsumer.core.httpservice.telegrambot.TelegramBotHttpService;

@Service("telegramBotServiceHttpClient")
@RequiredArgsConstructor
public class TelegramBotServiceHttpClient implements TelegramBotService {

  private final TelegramBotHttpService telegramBotHttpService;

  @Override
  public void sendCommunityMessage(CoubMessage communitySubscriptionMessage) {
    telegramBotHttpService.sendCoubCommunityMessage(communitySubscriptionMessage);
  }

  @Override
  public void sendTagMessage(CoubMessage tagSubscriptionMessage) {
    telegramBotHttpService.sendCoubTagMessage(tagSubscriptionMessage);
  }

  @Override
  public void sendChannelMessage(CoubMessage channelSubscriptionMessage) {
    telegramBotHttpService.sendCoubChannelMessage(channelSubscriptionMessage);
  }
}
