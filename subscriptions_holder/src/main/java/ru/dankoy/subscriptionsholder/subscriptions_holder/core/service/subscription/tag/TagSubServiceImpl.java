package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.subscription.tag;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.TagSub;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceConflictException;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository.subscription.tag.TagSubRepository;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.order.OrderService;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.scope.ScopeService;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.tag.TagService;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.telegramchat.TelegramChatService;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.type.TypeService;

@Service
@RequiredArgsConstructor
public class TagSubServiceImpl implements TagSubService {

  private final TagSubRepository tagSubRepository;
  private final TagService tagService;
  private final ScopeService scopeService;
  private final OrderService orderService;
  private final TypeService typeService;
  private final TelegramChatService telegramChatService;

  @Override
  public Page<TagSub> getAllByActiveTelegramChats(boolean active, Pageable pageable) {
    return tagSubRepository.findAllByChatActive(active, pageable);
  }

  @Override
  public List<TagSub> getAllByTelegramChatId(long telegramChatId) {
    return tagSubRepository.getAllByChatChatId(telegramChatId);
  }

  @Override
  public List<TagSub> getAllByTelegramChatIdAndMessageThreadId(
      long telegramChatId, Integer messageThreadId) {
    return tagSubRepository.getAllByChatChatIdAndChatMessageThreadId(
        telegramChatId, messageThreadId);
  }

  @Transactional
  @Override
  public TagSub createSubscription(TagSub tagSubscription) {

    // check existence
    var optional =
        tagSubRepository.getByChatChatIdAndChatMessageThreadIdAndTagTitleAndOrderValue(
            tagSubscription.getChat().getChatId(),
            tagSubscription.getChat().getMessageThreadId(),
            tagSubscription.getTag().getTitle(),
            tagSubscription.getOrder().getValue());

    // if exists throw exception
    optional.ifPresent(
        s -> {
          throw new ResourceConflictException(
              String.format(
                  "Subscription already exists for tag - %s", tagSubscription.getTag().getTitle()));
        });

    // Throws ResourceNotFoundException
    var tag = tagService.getByTitle(tagSubscription.getTag().getTitle());
    var scope = scopeService.getByName(tagSubscription.getScope().getName());
    var type = typeService.getByName(tagSubscription.getType().getName());
    var order =
        orderService.getByValueAndType(
            tagSubscription.getOrder().getValue(),
            tagSubscription.getOrder().getSubscriptionType().getType());

    // todo: do i even need to save chat when creating subscription?

    var optionalChat =
        telegramChatService.getByTelegramChatIdAndMessageThreadId(
            tagSubscription.getChat().getChatId(), tagSubscription.getChat().getMessageThreadId());

    if (optionalChat.isPresent()) {

      var chat = optionalChat.get();

      var newTagSubscription =
          TagSub.builder()
              .id(0)
              .tag(tag)
              .chat(chat)
              .chatUuid(tagSubscription.getChatUuid())
              .order(order)
              .scope(scope)
              .type(type)
              .lastPermalink(null)
              .build();

      return tagSubRepository.save(newTagSubscription);

    } else {

      var createdChat = telegramChatService.save(tagSubscription.getChat());

      var newTagSubscription =
          TagSub.builder()
              .id(0)
              .tag(tag)
              .chat(createdChat)
              .chatUuid(tagSubscription.getChatUuid())
              .order(order)
              .scope(scope)
              .type(type)
              .lastPermalink(null)
              .build();

      return tagSubRepository.save(newTagSubscription);
    }
  }

  @Transactional
  @Override
  public void deleteSubscription(TagSub tagSubscription) {

    var optional =
        tagSubRepository.getByChatChatIdAndChatMessageThreadIdAndTagTitleAndOrderValue(
            tagSubscription.getChat().getChatId(),
            tagSubscription.getChat().getMessageThreadId(),
            tagSubscription.getTag().getTitle(),
            tagSubscription.getOrder().getValue());

    optional.ifPresent(tagSubRepository::delete);
  }

  @Override
  public Page<TagSub> findAllByChatsUUID(List<UUID> chatUuids, Pageable pageable) {
    List<String> uuids = chatUuids.stream().map(UUID::toString).toList();
    return tagSubRepository.findAllBychatUuidIsIn(uuids, pageable);
  }
}
