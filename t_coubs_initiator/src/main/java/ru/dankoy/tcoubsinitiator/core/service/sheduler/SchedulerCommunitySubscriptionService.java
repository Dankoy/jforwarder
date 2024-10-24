package ru.dankoy.tcoubsinitiator.core.service.sheduler;

import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import ru.dankoy.tcoubsinitiator.core.domain.coubcom.coub.Coub;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.communitysubscription.CommunitySubscription;
import ru.dankoy.tcoubsinitiator.core.service.coubfinder.CoubFinderService;
import ru.dankoy.tcoubsinitiator.core.service.filter.FilterByRegistryService;
import ru.dankoy.tcoubsinitiator.core.service.messageproducerconnectorservice.MessageProducerCommunitySubscriptionService;
import ru.dankoy.tcoubsinitiator.core.service.subscription.SubscriptionService;

@Slf4j
@Service
public class SchedulerCommunitySubscriptionService
        extends SchedulerSubscriptionServiceTemplate<CommunitySubscription> {

    private final SubscriptionService communitySubscriptionService;
    private final MessageProducerCommunitySubscriptionService messageProducerCommunitySubscriptionService;

    public SchedulerCommunitySubscriptionService(SubscriptionService communitySubscriptionService,
            MessageProducerCommunitySubscriptionService messageProducerCommunitySubscriptionService,
            CoubFinderService coubFinderService, FilterByRegistryService filter) {
        super(coubFinderService, filter);
        this.communitySubscriptionService = communitySubscriptionService;
        this.messageProducerCommunitySubscriptionService = messageProducerCommunitySubscriptionService;
    }

    @Scheduled(initialDelay = 30_000, fixedRate = 6_000_000) // 100 mins
    @Override
    public void scheduledOperation() {
        super.scheduledOperation();
    }

    @Override
    public Page<CommunitySubscription> getActiveSubscriptions(Pageable pageable) {
        return communitySubscriptionService
                .getAllSubscriptionsWithActiveChats(pageable);
    }

    @Override
    public void findLastPermalinkSubs(Page<CommunitySubscription> page) {

        // поиск кубов из апи с last_permalink
        for (var subscription : page) {

            log.info("Working with subscription - '{}'", subscription);

            List<Coub> coubsToSend = coubFinderService.findUnsentCoubsForCommunitySubscription(subscription);

            // reverse coubs
            Collections.reverse(coubsToSend);

            subscription.addCoubs(coubsToSend);
        }

    }

    @Override
    public void send(List<CommunitySubscription> toSend) {

        if (!toSend.isEmpty()) {
            messageProducerCommunitySubscriptionService.sendCommunitySubscriptionsData(toSend);
        }

    }

}
