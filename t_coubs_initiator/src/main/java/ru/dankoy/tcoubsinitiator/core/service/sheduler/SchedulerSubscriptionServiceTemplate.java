package ru.dankoy.tcoubsinitiator.core.service.sheduler;

import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.dankoy.tcoubsinitiator.core.domain.coubcom.coub.Coub;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.subscription.Subscription;
import ru.dankoy.tcoubsinitiator.core.domain.telegramchatservice.Chat;
import ru.dankoy.tcoubsinitiator.core.domain.telegramchatservice.filter.TelegramChatFilter;
import ru.dankoy.tcoubsinitiator.core.service.coubfinder.CoubFinderService;
import ru.dankoy.tcoubsinitiator.core.service.filter.FilterByRegistryService;
import ru.dankoy.tcoubsinitiator.core.service.telegramchat.TelegramChatService;

@Slf4j
@RequiredArgsConstructor
public abstract class SchedulerSubscriptionServiceTemplate<T extends Subscription>
    implements SchedulerSubscriptionService {

  protected static final int FIRST_PAGE = 0;
  protected static final int PAGE_SIZE = 3;

  protected final CoubFinderService coubFinderService;
  protected final FilterByRegistryService filter;
  protected final TelegramChatService telegramChatService;

  @Override
  public void scheduledOperation() {

    int page = FIRST_PAGE;
    int totalPages = Integer.MAX_VALUE;

    // iterate by pages
    while (page <= totalPages) {

      var sort = Sort.by("id").ascending();
      var pageable = PageRequest.of(page, PAGE_SIZE, sort);

      var f = TelegramChatFilter.builder().active(true).build();

      Page<Chat> chats = getAllActiveChats(pageable, f);

      if (chats.isEmpty()) {
        log.info("no active chats found");
        totalPages = chats.getTotalPages();
        page++;
        continue;
      }

      processChats(chats.getContent());

      totalPages = chats.getTotalPages() - 1;

      page++;
    }
  }

  protected void processChats(List<Chat> chats) {

    int page = FIRST_PAGE;
    int totalPages = Integer.MAX_VALUE;

    var sort = Sort.by("id").ascending();

    do {

      var pageable = PageRequest.of(page, PAGE_SIZE, sort);

      var p = getActiveSubscriptions(chats, pageable);

      totalPages = p.getTotalPages() - 1;
      page++;

      logPageContent(p);
      findLastPermalinkSubs(p);
      filterByRegistry(p);

      List<T> toSend = removeSubscriptionsWithEmptyCoubs(p);

      send(toSend);
      logDone(page, totalPages, p);

    } while (page <= totalPages);
  }

  protected abstract Page<T> getActiveSubscriptions(List<Chat> chats, Pageable page);

  protected abstract void send(List<T> toSend);

  protected abstract List<Coub> findUnsentCoubsForSubscription(T subscription);

  protected Page<Chat> getAllActiveChats(Pageable pageable, TelegramChatFilter filter) {
    return telegramChatService.getAllChats(pageable, filter);
  }

  protected void findLastPermalinkSubs(Page<T> page) {

    // поиск кубов из апи с last_permalink
    for (var subscription : page) {

      log.info("Working with subscription - '{}'", subscription);

      List<Coub> coubsToSend = findUnsentCoubsForSubscription(subscription);

      // reverse coubs
      Collections.reverse(coubsToSend);

      subscription.addCoubs(coubsToSend);
    }
  }

  protected void filterByRegistry(Page<T> page) {
    filter.filterByRegistry(page.getContent());
  }

  protected List<T> removeSubscriptionsWithEmptyCoubs(Page<T> page) {
    // remove subscriptions without coubs

    var toSend = page.stream().filter(s -> !s.getCoubs().isEmpty()).toList();

    // send to message producer service

    log.info("Coubs to send for all subscriptions - {}", toSend);

    return toSend;
  }

  protected void logPageContent(Page<T> subs) {

    log.info("{} - {}", subs, subs.getContent());
  }

  protected void logDone(int page, int totalPages, Page<T> subs) {

    log.info("Page {} of {} is done", page, totalPages);
    log.info("Amount of subscriptions processed: {}", subs.getContent().size());
  }
}
