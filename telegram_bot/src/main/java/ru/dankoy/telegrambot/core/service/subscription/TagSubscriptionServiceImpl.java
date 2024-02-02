package ru.dankoy.telegrambot.core.service.subscription;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.telegrambot.core.domain.subscription.Chat;
import ru.dankoy.telegrambot.core.domain.tagsubscription.Order;
import ru.dankoy.telegrambot.core.domain.tagsubscription.Scope;
import ru.dankoy.telegrambot.core.domain.tagsubscription.Tag;
import ru.dankoy.telegrambot.core.domain.tagsubscription.TagSubscription;
import ru.dankoy.telegrambot.core.domain.tagsubscription.Type;
import ru.dankoy.telegrambot.core.exceptions.NotFoundException;
import ru.dankoy.telegrambot.core.service.coubtags.CoubTagsSearcherService;
import ru.dankoy.telegrambot.core.service.tag.TagService;


@RequiredArgsConstructor
@Service
public class TagSubscriptionServiceImpl implements TagSubscriptionService {

  private final CoubTagsSearcherService coubTagsSearcherService;

  private final TagService tagService;

  @Override
  public List<TagSubscription> getSubscriptionsByChatId(long chatId) {
    return tagService.getAllSubscriptionsByChat(chatId);
  }

  @Override
  public TagSubscription subscribe(
      String tagName,
      String orderValue,
      String scopeName,
      String typeName,
      long chatId) {

    // 1. find tag in db
    var optionalTagFromDb = tagService.findTagByTitle(tagName);

    if (optionalTagFromDb.isPresent()) {
      var tag = optionalTagFromDb.get();
      var tagSubscription = new TagSubscription(
          0,
          tag,
          new Chat(chatId),
          new Order(orderValue),
          new Scope(scopeName),
          new Type(typeName),
          null,
          new ArrayList<>()
      );

      return tagService.subscribeByTag(tagSubscription);

    } else {

      var optionalTagFromApi = coubTagsSearcherService.findByTitle(tagName);

      if (optionalTagFromApi.isPresent()) {

        var tag = optionalTagFromApi.get();

        var created = tagService.create(tag);

        var tagSubscription = new TagSubscription(
            0,
            new Tag(created.getTitle()),
            new Chat(chatId),
            new Order(orderValue),
            new Scope(scopeName),
            new Type(typeName),
            null,
            new ArrayList<>()
        );

        return tagService.subscribeByTag(tagSubscription);

      } else {
        throw new NotFoundException(
            String.format("Tag '%s' not found. Validate tag name and try again.", tagName)
        );
      }

    }

  }

  @Override
  public void unsubscribe(
      String tagName,
      String orderValue,
      String scopeName,
      String typeName,
      long chatId
  ) {

    var tagSubscription = new TagSubscription(
        0,
        new Tag(tagName),
        new Chat(chatId),
        new Order(orderValue),
        new Scope(scopeName),
        new Type(typeName),
        null,
        new ArrayList<>()
    );

    tagService.unsubscribeByTag(tagSubscription);

  }
}
