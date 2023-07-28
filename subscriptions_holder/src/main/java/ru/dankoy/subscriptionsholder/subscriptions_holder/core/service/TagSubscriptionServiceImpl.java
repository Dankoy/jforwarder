package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.TagSubscription;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceConflictException;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceNotFoundException;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository.TagSubscriptionRepository;


@Service
@RequiredArgsConstructor
public class TagSubscriptionServiceImpl implements TagSubscriptionService {

  private final TagSubscriptionRepository tagSubscriptionRepository;
  private final TagService tagService;
  private final TelegramChatService telegramChatService;

  @Override
  public Optional<TagSubscription> getByTagTitleAndTelegramChatId(String title,
      long telegramChatId) {
    return tagSubscriptionRepository.getByChatChatIdAndTagTitle(telegramChatId, title);
  }

  @Override
  public List<TagSubscription> getAllByActiveTelegramChat(boolean active) {
    return tagSubscriptionRepository.findAllWithActiveChats(active);
  }

  @Transactional
  @Override
  public TagSubscription createSubscription(TagSubscription tagSubscription) {

    // check existence
    var optional = tagSubscriptionRepository.getByChatChatIdAndTagTitle(
        tagSubscription.getChat().getChatId(),
        tagSubscription.getTag().getTitle()
    );

    // if exists throw exception
    optional.ifPresent(s -> {
          throw new ResourceConflictException(
              String.format("Subscription already exists for tag - %s",
                  tagSubscription.getTag().getTitle()));
        }
    );

    // check if tag exists. Throws ResourceNotFoundException
    var tag = tagService.getByTitle(tagSubscription.getTag().getTitle());

    // todo: do i even need to save chat when creating subscription?

    var optionalChat = telegramChatService.getByTelegramChatId(
        tagSubscription.getChat().getChatId());

    if (optionalChat.isPresent()) {

      var chat = optionalChat.get();

      tagSubscription.setChat(chat);
      tagSubscription.setTag(tag);

      return tagSubscriptionRepository.save(tagSubscription);

    } else {

      var createdChat = telegramChatService.save(tagSubscription.getChat());

      tagSubscription.setChat(createdChat);
      tagSubscription.setTag(tag);

      return tagSubscriptionRepository.save(tagSubscription);

    }

  }

  @Transactional
  @Override
  public void deleteSubscription(TagSubscription tagSubscription) {

    var optional = tagSubscriptionRepository.getByChatChatIdAndTagTitle(
        tagSubscription.getChat().getChatId(),
        tagSubscription.getTag().getTitle()
    );

    optional.ifPresent(tagSubscriptionRepository::delete);

  }

  @Override
  public TagSubscription updatePermalink(TagSubscription tagSubscription) {
    var optional = tagSubscriptionRepository.getByChatChatIdAndTagTitle(
        tagSubscription.getChat().getChatId(),
        tagSubscription.getTag().getTitle()
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
