package ru.dankoy.telegrambot.core.service.subscription;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.telegrambot.core.domain.Chat;
import ru.dankoy.telegrambot.core.domain.subscription.Order;
import ru.dankoy.telegrambot.core.domain.subscription.Scope;
import ru.dankoy.telegrambot.core.domain.subscription.SubscriptionType;
import ru.dankoy.telegrambot.core.domain.subscription.Type;
import ru.dankoy.telegrambot.core.domain.subscription.tag.Tag;
import ru.dankoy.telegrambot.core.domain.subscription.tag.TagSubscription;
import ru.dankoy.telegrambot.core.exceptions.ExceptionObjectType;
import ru.dankoy.telegrambot.core.exceptions.NotFoundException;
import ru.dankoy.telegrambot.core.service.coubtags.CoubSmartSearcherService;
import ru.dankoy.telegrambot.core.service.order.OrderService;
import ru.dankoy.telegrambot.core.service.tag.TagService;

@RequiredArgsConstructor
@Service
public class TagSubscriptionServiceImpl implements TagSubscriptionService {

  private final CoubSmartSearcherService coubSmartSearcherService;

  private final TagService tagService;

  private final OrderService orderService;

  /**
   * @deprecated for topics support via messageThreadId
   */
  @Deprecated(since = "2024-04-05", forRemoval = true)
  @Override
  public List<TagSubscription> getSubscriptionsByChatId(long chatId) {
    return tagService.getAllSubscriptionsByChat(chatId);
  }

  @Override
  public List<TagSubscription> getSubscriptionsByChatIdAndMessageThreadId(
      long chatId, Integer messageThreadId) {
    return tagService.getAllSubscriptionsByChatIdAndMessageThreadId(chatId, messageThreadId);
  }

  @Override
  public TagSubscription subscribe(
      String tagName,
      String orderValue,
      String scopeName,
      String typeName,
      long chatId,
      Integer messageThreadId) {

    // 1. Find tag order

    var optionalTagOrder = orderService.findByValue(orderValue, SubscriptionType.TAG);

    var order =
        optionalTagOrder.orElseThrow(
            () ->
                new NotFoundException(
                    ExceptionObjectType.ORDER,
                    orderValue,
                    String.format(
                        "Order '%s' not found. Validate tag order and try" + " again",
                        orderValue)));

    order.setSubscriptionType(SubscriptionType.TAG);

    // 2. find tag in db
    var optionalTagFromDb = tagService.findTagByTitle(tagName);

    if (optionalTagFromDb.isPresent()) {
      var tag = optionalTagFromDb.get();
      var tagSubscription =
          (TagSubscription)
              TagSubscription.builder()
                  .id(0)
                  .tag(tag)
                  .chat(new Chat(chatId, messageThreadId))
                  .order(order)
                  .scope(new Scope(scopeName))
                  .type(new Type(typeName))
                  .build();

      return tagService.subscribeByTag(tagSubscription);

    } else {

      var optionalTagFromApi = coubSmartSearcherService.findTagByTitle(tagName);

      if (optionalTagFromApi.isPresent()) {

        var tag = optionalTagFromApi.get();

        var created = tagService.create(tag);

        var tagSubscription =
            (TagSubscription)
                TagSubscription.builder()
                    .id(0)
                    .tag(created)
                    .chat(new Chat(chatId, messageThreadId))
                    .order(order)
                    .scope(new Scope(scopeName))
                    .type(new Type(typeName))
                    .build();

        return tagService.subscribeByTag(tagSubscription);

      } else {
        throw new NotFoundException(
            ExceptionObjectType.TAG,
            tagName,
            String.format("Tag '%s' not found. Validate tag name and try again.", tagName));
      }
    }
  }

  @Override
  public void unsubscribe(
      String tagName,
      String orderValue,
      String scopeName,
      String typeName,
      long chatId,
      Integer messageThreadId) {

    var order = new Order(orderValue);
    order.setSubscriptionType(SubscriptionType.TAG);

    var tagSubscription =
        (TagSubscription)
            TagSubscription.builder()
                .id(0)
                .tag(new Tag(tagName))
                .chat(new Chat(chatId, messageThreadId))
                .order(order)
                .scope(new Scope(scopeName))
                .type(new Type(typeName))
                .build();

    tagService.unsubscribeByTag(tagSubscription);
  }
}
