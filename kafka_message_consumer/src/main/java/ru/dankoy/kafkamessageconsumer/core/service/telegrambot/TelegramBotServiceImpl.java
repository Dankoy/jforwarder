package ru.dankoy.kafkamessageconsumer.core.service.telegrambot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.kafkamessageconsumer.core.domain.message.CommunitySubscriptionMessage;
import ru.dankoy.kafkamessageconsumer.core.feign.telegrambot.TelegramBotFeign;

@Service
@RequiredArgsConstructor
public class TelegramBotServiceImpl implements TelegramBotService {

  private final TelegramBotFeign telegramBotFeign;

  @Override
  public void sendMessage(CommunitySubscriptionMessage communitySubscriptionMessage) {
    telegramBotFeign.send(communitySubscriptionMessage);
  }
}
