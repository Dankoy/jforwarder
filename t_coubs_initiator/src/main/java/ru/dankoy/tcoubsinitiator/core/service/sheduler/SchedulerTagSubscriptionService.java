package ru.dankoy.tcoubsinitiator.core.service.sheduler;

import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import ru.dankoy.tcoubsinitiator.core.domain.coubcom.coub.Coub;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.tagsubscription.TagSubscription;
import ru.dankoy.tcoubsinitiator.core.service.coubfinder.CoubFinderService;
import ru.dankoy.tcoubsinitiator.core.service.filter.FilterByRegistryService;
import ru.dankoy.tcoubsinitiator.core.service.messageproducerconnectorservice.MessageProducerTagSubscriptionService;
import ru.dankoy.tcoubsinitiator.core.service.tagsubscription.TagSubscriptionService;

@Slf4j
@Service
public class SchedulerTagSubscriptionService
        extends SchedulerSubscriptionServiceTemplate<TagSubscription> {

    private final TagSubscriptionService tagSubscriptionService;
    private final MessageProducerTagSubscriptionService messageProducerTagSubscriptionService;

    public SchedulerTagSubscriptionService(TagSubscriptionService tagSubscriptionService,
            MessageProducerTagSubscriptionService messageProducerTagSubscriptionService,
            CoubFinderService coubFinderService, FilterByRegistryService filter) {
        super(coubFinderService, filter);
        this.tagSubscriptionService = tagSubscriptionService;
        this.messageProducerTagSubscriptionService = messageProducerTagSubscriptionService;
    }

    @Scheduled(initialDelay = 90_000, fixedRate = 6_000_000) // 100 mins
    @Override
    public void scheduledOperation() {
        super.scheduledOperation();
    }

    @Override
    public Page<TagSubscription> getActiveSubscriptions(Pageable pageable) {
        return tagSubscriptionService
                .getAllSubscriptionsWithActiveChats(pageable);
    }

    @Override
    public void findLastPermalinkSubs(Page<TagSubscription> page) {

        // поиск кубов из апи с last_permalink
        for (var subscription : page) {

            log.info("Working with subscription - '{}'", subscription);

            List<Coub> coubsToSend = coubFinderService.findUnsentCoubsForTagSubscription(subscription);

            // reverse coubs
            Collections.reverse(coubsToSend);

            subscription.addCoubs(coubsToSend);
        }

    }

    @Override
    public void send(List<TagSubscription> toSend) {

        if (!toSend.isEmpty()) {
            messageProducerTagSubscriptionService.sendTagSubscriptionsData(toSend);
        }

    }

}
