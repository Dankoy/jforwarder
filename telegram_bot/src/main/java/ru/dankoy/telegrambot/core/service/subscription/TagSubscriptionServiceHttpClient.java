package ru.dankoy.telegrambot.core.service.subscription;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
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
import ru.dankoy.telegrambot.core.httpservice.subscriptionsholder.SubscriptionsHolderTagSubHttpService;
import ru.dankoy.telegrambot.core.service.chat.TelegramChatService;
import ru.dankoy.telegrambot.core.service.coubtags.CoubSmartSearcherService;
import ru.dankoy.telegrambot.core.service.order.OrderService;
import ru.dankoy.telegrambot.core.service.tag.TagService;

@RequiredArgsConstructor
@Service
public class TagSubscriptionServiceHttpClient implements TagSubscriptionService {

  @Qualifier("coubSmartSearcherServiceHttpClient")
  private final CoubSmartSearcherService coubSmartSearcherService;

  @Qualifier("tagServiceHttpClient")
  private final TagService tagService;

  @Qualifier("orderServiceHttpClient")
  private final OrderService orderService;

  @Qualifier("telegramChatServiceHttpClient")
  private final TelegramChatService telegramChatService;

  private final SubscriptionsHolderTagSubHttpService tagSubsHttpService;

  /**
   * @deprecated for topics support via messageThreadId
   */
  @Deprecated(since = "2024-04-05", forRemoval = true)
  @Override
  public List<TagSubscription> getSubscriptionsByChatId(long chatId) {
    return tagService.getAllSubscriptionsByChat(chatId);
  }

  /**
   * @deprecated chat is in separate microservice and db
   */
  @Deprecated(since = "2025-02-28", forRemoval = false)
  @Override
  public List<TagSubscription> getSubscriptionsByChatIdAndMessageThreadId(
      long chatId, Integer messageThreadId) {
    return tagService.getAllSubscriptionsByChatIdAndMessageThreadId(chatId, messageThreadId);
  }

  @Override
  public List<TagSubscription> getSubsByChatIdAndMessageThreadId(
      long chatId, Integer messageThreadId) {

    var chat = telegramChatService.getChatByIdAndMessageThreadId(chatId, messageThreadId);

    return tagSubsHttpService.getAllTagSubscriptionsByChatUuid(chat.getId());
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

    // get chat from separate microservice
    var chat = telegramChatService.getChatByIdAndMessageThreadId(chatId, messageThreadId);

    var jpaChat =
        Chat.builder()
            .id(0)
            .chatId(chatId)
            .messageThreadId(messageThreadId)
            .active(true)
            .firstName(chat.getFirstName())
            .lastName(chat.getLastName())
            .username(chat.getUsername())
            .type(chat.getType())
            .title(chat.getTitle())
            .build();

    if (optionalTagFromDb.isPresent()) {
      var tag = optionalTagFromDb.get();
      var tagSubscription =
          (TagSubscription)
              TagSubscription.builder()
                  .id(0)
                  .tag(tag)
                  .chat(jpaChat)
                  .chatUuid(chat.getId())
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
                    .chatUuid(chat.getId())
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

    // get chat from separate microservice
    var chat = telegramChatService.getChatByIdAndMessageThreadId(chatId, messageThreadId);

    var tagSubscription =
        (TagSubscription)
            TagSubscription.builder()
                .id(0)
                .tag(new Tag(tagName))
                .chat(new Chat(chatId, messageThreadId))
                .chatUuid(chat.getId())
                .order(order)
                .scope(new Scope(scopeName))
                .type(new Type(typeName))
                .build();

    tagService.unsubscribeByTag(tagSubscription);
  }
}
