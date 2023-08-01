package ru.dankoy.tcoubsinitiator.core.service.coubfinder;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.dankoy.tcoubsinitiator.core.domain.coubcom.coub.Coub;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.communitysubscription.CommunitySubscription;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.tagsubscription.TagSubscription;
import ru.dankoy.tcoubsinitiator.core.service.coub.CoubService;

@Slf4j
@RequiredArgsConstructor
@Service
public class CoubFinderServiceImpl implements CoubFinderService {

  private static final long FIRST_PAGE = 1;
  private static final int PER_PAGE = 10;
  private static final long MAX_PAGE_TO_TRY = 2;
  private static final int LIMIT_AMOUNT = 3;

  private final CoubService coubService;

  @Override
  public List<Coub> findUnsentCoubsForCommunitySubscription(
      CommunitySubscription communitySubscription) {

    List<Coub> allCoubs = new ArrayList<>();

    long page = FIRST_PAGE;
    int perPage = PER_PAGE;

    var wrapper = coubService.getCoubsWrapperForCommunityAndSection(
        communitySubscription.getCommunity().getName(),
        communitySubscription.getSection().getName(),
        page,
        perPage
    );

    List<Coub> firstSetOfCoubs = new ArrayList<>(wrapper.getCoubs());

    long totalPages = wrapper.getTotalPages();

    var lastPermalink = communitySubscription.getLastPermalink();

    if (Objects.isNull(lastPermalink) || lastPermalink.isEmpty()) {

      return limitCoubs(firstSetOfCoubs, LIMIT_AMOUNT);

    }

    while (page <= totalPages) {

      allCoubs.addAll(wrapper.getCoubs());
      allCoubs.sort((coub1, coub2) -> coub2.getPublishedAt().compareTo(coub1.getPublishedAt()));

      Optional<Coub> optionalLastCoubOnPage = allCoubs.stream()
          .filter(c -> c.getPermalink().equals(lastPermalink))
          .findFirst();

      if (optionalLastCoubOnPage.isEmpty()) {

        if (page == MAX_PAGE_TO_TRY) {
          return limitCoubs(firstSetOfCoubs, LIMIT_AMOUNT);
        }

        page++;

        log.info("Coub with last permalink '{}' not found", lastPermalink);
        log.info("Trying page: {}", page);

        sleep(3_000);

        wrapper = coubService.getCoubsWrapperForCommunityAndSection(
            communitySubscription.getCommunity().getName(),
            communitySubscription.getSection().getName(),
            page,
            perPage
        );

      } else {

        log.info("Found coub with lastPermalink in current list");
        log.info("Trying to sort and find unsent coubs");

        var lastCoub = optionalLastCoubOnPage.get();

        deleteOlderCoubs(allCoubs, lastCoub);

        log.debug("Coubs to send - {}", allCoubs);

        return allCoubs;

      }


    }

    return firstSetOfCoubs;
  }

  @Override
  public List<Coub> findUnsentCoubsForTagSubscription(TagSubscription tagSubscription) {
    List<Coub> allCoubs = new ArrayList<>();

    long page = FIRST_PAGE;
    int perPage = PER_PAGE;

    var wrapper = coubService.getCoubsWrapperForTag(
        tagSubscription.getTag().getTitle(),
        tagSubscription.getOrder().getName(),
        tagSubscription.getType().getName(),
        tagSubscription.getScope().getName(),
        page,
        perPage
    );

    long totalPages = wrapper.getTotalPages();
    List<Coub> firstSetOfCoubs = new ArrayList<>(wrapper.getCoubs());

    var lastPermalink = tagSubscription.getLastPermalink();

    if (Objects.isNull(lastPermalink) || lastPermalink.isEmpty()) {

      return limitCoubs(firstSetOfCoubs, LIMIT_AMOUNT);

    }

    while (page <= totalPages) {

      allCoubs.addAll(wrapper.getCoubs());
      allCoubs.sort((coub1, coub2) -> coub2.getPublishedAt().compareTo(coub1.getPublishedAt()));

      Optional<Coub> optionalLastCoubOnPage = allCoubs.stream()
          .filter(c -> c.getPermalink().equals(lastPermalink))
          .findFirst();

      if (optionalLastCoubOnPage.isEmpty()) {

        if (page == MAX_PAGE_TO_TRY) {
          return limitCoubs(firstSetOfCoubs, LIMIT_AMOUNT);
        }

        page++;

        log.info("Coub with last permalink '{}' not found", lastPermalink);
        log.info("Trying page: {}", page);

        sleep(5_000);

        wrapper = coubService.getCoubsWrapperForTag(
            tagSubscription.getTag().getTitle(),
            tagSubscription.getOrder().getName(),
            tagSubscription.getType().getName(),
            tagSubscription.getScope().getName(),
            page,
            perPage
        );

      } else {

        log.info("Found coub with lastPermalink in current list");
        log.info("Trying to sort and find unsent coubs");

        var lastCoub = optionalLastCoubOnPage.get();

        deleteOlderCoubs(allCoubs, lastCoub);

        log.debug("Coubs to send - {}", allCoubs);

        return allCoubs;

      }


    }

    return firstSetOfCoubs;
  }


  private void deleteOlderCoubs(List<Coub> coubs, Coub lastCoub) {

    coubs.remove(lastCoub);
    coubs.removeIf(c -> c.getPublishedAt().isBefore(lastCoub.getPublishedAt()));

  }

  private List<Coub> useCoubsFromFirstPage(List<Coub> coubs) {
    coubs.sort((coub1, coub2) -> coub2.getPublishedAt().compareTo(coub1.getPublishedAt()));
    return coubs;
  }

  private List<Coub> limitCoubs(List<Coub> coubs, int limit) {
    coubs.sort((coub1, coub2) -> coub2.getPublishedAt().compareTo(coub1.getPublishedAt()));
    return coubs.subList(0, limit);
  }

  private void sleep(long millis) {

    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException("Interrupted while trying to get coubs", e);
    }

  }
}
