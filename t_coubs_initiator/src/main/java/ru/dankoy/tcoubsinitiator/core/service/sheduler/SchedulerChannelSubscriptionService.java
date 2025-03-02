package ru.dankoy.tcoubsinitiator.core.service.sheduler;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.dankoy.tcoubsinitiator.core.domain.coubcom.coub.Coub;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.channelsubscription.ChannelSubscription;
import ru.dankoy.tcoubsinitiator.core.domain.telegramchatservice.Chat;
import ru.dankoy.tcoubsinitiator.core.service.channelsubscription.ChannelSubscriptionService;
import ru.dankoy.tcoubsinitiator.core.service.coubfinder.CoubFinderService;
import ru.dankoy.tcoubsinitiator.core.service.filter.FilterByRegistryService;
import ru.dankoy.tcoubsinitiator.core.service.messageproducerconnectorservice.MessageProducerChannelSubscriptionService;
import ru.dankoy.tcoubsinitiator.core.service.telegramchat.TelegramChatService;

@Slf4j
@Service
public class SchedulerChannelSubscriptionService
    extends SchedulerSubscriptionServiceTemplate<ChannelSubscription> {

  private final ChannelSubscriptionService channelSubscriptionService;
  private final MessageProducerChannelSubscriptionService messageProducerChannelSubscriptionService;

  public SchedulerChannelSubscriptionService(
      ChannelSubscriptionService channelSubscriptionService,
      MessageProducerChannelSubscriptionService messageProducerChannelSubscriptionService,
      CoubFinderService coubFinderService,
      FilterByRegistryService filter,
      TelegramChatService telegramChatService) {
    super(coubFinderService, filter, telegramChatService);
    this.channelSubscriptionService = channelSubscriptionService;
    this.messageProducerChannelSubscriptionService = messageProducerChannelSubscriptionService;
  }

  @Scheduled(initialDelay = 120_000, fixedRate = 6_000_000) // 100 mins
  @Override
  public void scheduledOperation() {
    super.scheduledOperation();
  }

  @Override
  public Page<ChannelSubscription> getActiveSubscriptions(List<Chat> chats, Pageable pageable) {
    var uuids = chats.stream().map(Chat::getId).toList();

    return channelSubscriptionService.getAllSubscriptionsByChatUuid(uuids, pageable);
  }

  @Override
  protected List<Coub> findUnsentCoubsForSubscription(ChannelSubscription subscription) {

    return coubFinderService.findUnsentCoubsForChannelSubscription(subscription);
  }

  @Override
  public void send(List<ChannelSubscription> toSend) {

    if (!toSend.isEmpty()) {
      messageProducerChannelSubscriptionService.sendChannelSubscriptionsData(toSend);
    }
  }
}
