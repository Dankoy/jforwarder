package ru.dankoy.tcoubsinitiator.core.service.sheduler;

import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.dankoy.tcoubsinitiator.core.domain.coubcom.coub.Coub;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.channelsubscription.ChannelSubscription;
import ru.dankoy.tcoubsinitiator.core.service.channelsubscription.ChannelSubscriptionService;
import ru.dankoy.tcoubsinitiator.core.service.coubfinder.CoubFinderService;
import ru.dankoy.tcoubsinitiator.core.service.filter.FilterByRegistryService;
import ru.dankoy.tcoubsinitiator.core.service.messageproducerconnectorservice.MessageProducerChannelSubscriptionService;

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
      FilterByRegistryService filter) {
    super(coubFinderService, filter);
    this.channelSubscriptionService = channelSubscriptionService;
    this.messageProducerChannelSubscriptionService = messageProducerChannelSubscriptionService;
  }

  @Scheduled(initialDelay = 120_000, fixedRate = 6_000_000) // 100 mins
  @Override
  public void scheduledOperation() {
    super.scheduledOperation();
  }

  @Override
  public Page<ChannelSubscription> getActiveSubscriptions(Pageable pageable) {
    return channelSubscriptionService.getAllSubscriptionsWithActiveChats(pageable);
  }

  @Override
  public void findLastPermalinkSubs(Page<ChannelSubscription> page) {

    // поиск кубов из апи с last_permalink
    for (var subscription : page) {

      log.info("Working with subscription - '{}'", subscription);

      List<Coub> coubsToSend =
          coubFinderService.findUnsentCoubsForChannelSubscription(subscription);

      // reverse coubs
      Collections.reverse(coubsToSend);

      subscription.addCoubs(coubsToSend);
    }
  }

  @Override
  public void send(List<ChannelSubscription> toSend) {

    if (!toSend.isEmpty()) {
      messageProducerChannelSubscriptionService.sendChannelSubscriptionsData(toSend);
    }
  }
}
