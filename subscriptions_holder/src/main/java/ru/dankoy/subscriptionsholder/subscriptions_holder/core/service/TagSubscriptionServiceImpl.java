package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.tag.TagSubscription;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceConflictException;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceNotFoundException;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository.TagSubscriptionRepository;

/**
 * @deprecated in favor for {@link TagSubServiceImpl}
 */
@Deprecated(since = "2024-01-27")
@Service
@RequiredArgsConstructor
public class TagSubscriptionServiceImpl implements TagSubscriptionService {

  private final TagSubscriptionRepository tagSubscriptionRepository;
  private final TagService tagService;
  private final ScopeService scopeService;
  private final OrderService orderService;
  private final TypeService typeService;
  private final TelegramChatService telegramChatService;

  @Override
  public Page<TagSubscription> getAllByActiveTelegramChats(boolean active, Pageable pageable) {
    return tagSubscriptionRepository.findAllByChatActive(active, pageable);
  }

  @Override
  public List<TagSubscription> getAllByTelegramChatId(long telegramChatId) {
    return tagSubscriptionRepository.getAllByChatChatId(telegramChatId);
  }

  @Transactional
  @Override
  public TagSubscription createSubscription(TagSubscription tagSubscription) {

    // check existence
    var optional = tagSubscriptionRepository.getByChatChatIdAndTagTitleAndOrderName(
        tagSubscription.getChat().getChatId(),
        tagSubscription.getTag().getTitle(),
        tagSubscription.getOrder().getName()
    );

    // if exists throw exception
    optional.ifPresent(s -> {
          throw new ResourceConflictException(
              String.format("Subscription already exists for tag - %s",
                  tagSubscription.getTag().getTitle()));
        }
    );

    // Throws ResourceNotFoundException
    var tag = tagService.getByTitle(tagSubscription.getTag().getTitle());
    var scope = scopeService.getByName(tagSubscription.getScope().getName());
    var type = typeService.getByName(tagSubscription.getType().getName());
    var order = orderService.getByName(tagSubscription.getOrder().getName());

    // todo: do i even need to save chat when creating subscription?

    var optionalChat = telegramChatService.getByTelegramChatId(
        tagSubscription.getChat().getChatId());

    if (optionalChat.isPresent()) {

      var chat = optionalChat.get();

      var newTagSubscription = new TagSubscription(
          0,
          tag,
          chat,
          order,
          scope,
          type,
          null
      );

      return tagSubscriptionRepository.save(newTagSubscription);

    } else {

      var createdChat = telegramChatService.save(tagSubscription.getChat());

      var newTagSubscription = new TagSubscription(
          0,
          tag,
          createdChat,
          order,
          scope,
          type,
          null
      );

      return tagSubscriptionRepository.save(newTagSubscription);

    }

  }

  @Transactional
  @Override
  public void deleteSubscription(TagSubscription tagSubscription) {

    var optional = tagSubscriptionRepository.getByChatChatIdAndTagTitleAndOrderName(
        tagSubscription.getChat().getChatId(),
        tagSubscription.getTag().getTitle(),
        tagSubscription.getOrder().getName()
    );

    optional.ifPresent(tagSubscriptionRepository::delete);

  }

  @Override
  public TagSubscription updatePermalink(TagSubscription tagSubscription) {
    var optional = tagSubscriptionRepository.getByChatChatIdAndTagTitleAndOrderName(
        tagSubscription.getChat().getChatId(),
        tagSubscription.getTag().getTitle(),
        tagSubscription.getOrder().getName()
    );

    var found = optional.orElseThrow(
        () -> new ResourceNotFoundException(
            String.format("Subscription not found: %s", tagSubscription)
        )
    );

    found.setLastPermalink(tagSubscription.getLastPermalink());

    return tagSubscriptionRepository.save(found);

  }
}
