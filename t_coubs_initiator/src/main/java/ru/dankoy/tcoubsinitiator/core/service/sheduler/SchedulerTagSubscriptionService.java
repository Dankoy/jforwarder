package ru.dankoy.tcoubsinitiator.core.service.sheduler;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.dankoy.tcoubsinitiator.core.domain.coubcom.coub.Coub;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.tagsubscription.TagSubscription;
import ru.dankoy.tcoubsinitiator.core.domain.telegramchatservice.Chat;
import ru.dankoy.tcoubsinitiator.core.service.coubfinder.CoubFinderService;
import ru.dankoy.tcoubsinitiator.core.service.filter.FilterByRegistryService;
import ru.dankoy.tcoubsinitiator.core.service.messageproducerconnectorservice.MessageProducerTagSubscriptionService;
import ru.dankoy.tcoubsinitiator.core.service.tagsubscription.TagSubscriptionService;
import ru.dankoy.tcoubsinitiator.core.service.telegramchat.TelegramChatService;

@Slf4j
@Service
public class SchedulerTagSubscriptionService
    extends SchedulerSubscriptionServiceTemplate<TagSubscription> {

  private final TagSubscriptionService tagSubscriptionService;
  private final MessageProducerTagSubscriptionService messageProducerTagSubscriptionService;

  public SchedulerTagSubscriptionService(
      TagSubscriptionService tagSubscriptionService,
      MessageProducerTagSubscriptionService messageProducerTagSubscriptionService,
      CoubFinderService coubFinderService,
      FilterByRegistryService filter,
      TelegramChatService telegramChatService) {
    super(coubFinderService, filter, telegramChatService);
    this.tagSubscriptionService = tagSubscriptionService;
    this.messageProducerTagSubscriptionService = messageProducerTagSubscriptionService;
  }

  @Scheduled(initialDelay = 90_000, fixedRate = 6_000_000) // 100 mins
  @Override
  public void scheduledOperation() {
    super.scheduledOperation();
  }

  @Override
  protected Page<TagSubscription> getActiveSubscriptions(List<Chat> chats, Pageable pageable) {

    var uuids = chats.stream().map(Chat::getId).toList();

    return tagSubscriptionService.getAllSubscriptionsByChatUuid(uuids, pageable);
  }

  @Override
  protected List<Coub> findUnsentCoubsForSubscription(TagSubscription subscription) {

    return coubFinderService.findUnsentCoubsForTagSubscription(subscription);
  }

  @Override
  protected void send(List<TagSubscription> toSend) {

    if (!toSend.isEmpty()) {
      messageProducerTagSubscriptionService.sendTagSubscriptionsData(toSend);
    }
  }
}
