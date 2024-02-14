package ru.dankoy.kafkamessageconsumer.core.service.telegrambot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.kafkamessageconsumer.core.domain.message.CoubMessage;
import ru.dankoy.kafkamessageconsumer.core.feign.telegrambot.TelegramBotFeign;

@Service
@RequiredArgsConstructor
public class TelegramBotServiceImpl implements TelegramBotService {

  private final TelegramBotFeign telegramBotFeign;

  @Override
  public void sendCommunityMessage(CoubMessage communitySubscriptionMessage) {
    telegramBotFeign.sendCoubCommunityMessage(communitySubscriptionMessage);
  }

  @Override
  public void sendTagMessage(CoubMessage tagSubscriptionMessage) {
    telegramBotFeign.sendCoubTagMessage(tagSubscriptionMessage);
  }

  @Override
  public void sendChannelMessage(CoubMessage channelSubscriptionMessage) {
    telegramBotFeign.sendCoubTagMessage(channelSubscriptionMessage);
  }
}
