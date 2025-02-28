package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.ChannelSub;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceConflictException;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository.ChannelSubRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelSubServiceImpl implements ChannelSubService {

  private final ChannelSubRepository channelSubRepository;
  private final ChannelService channelService;
  private final ScopeService scopeService;
  private final OrderService orderService;
  private final TypeService typeService;
  private final TelegramChatService telegramChatService;

  @Override
  public Page<ChannelSub> getAllByActiveTelegramChats(boolean active, Pageable pageable) {
    return channelSubRepository.findAllByChatActive(active, pageable);
  }

  @Override
  public List<ChannelSub> getAllByTelegramChatId(long telegramChatId) {
    return channelSubRepository.getAllByChatChatId(telegramChatId);
  }

  @Override
  public List<ChannelSub> getAllByTelegramChatIdAndMessageThreadId(
      long telegramChatId, Integer messageThreadId) {
    return channelSubRepository.getAllByChatChatIdAndChatMessageThreadId(
        telegramChatId, messageThreadId);
  }

  @Transactional
  @Override
  public ChannelSub createSubscription(ChannelSub channelSub) {

    // check existence
    var optional =
        channelSubRepository.getByChatChatIdAndChatMessageThreadIdAndChannelPermalinkAndOrderValue(
            channelSub.getChat().getChatId(),
            channelSub.getChat().getMessageThreadId(),
            channelSub.getChannel().getPermalink(),
            channelSub.getOrder().getValue());

    // if exists throw exception
    optional.ifPresent(
        s -> {
          throw new ResourceConflictException(
              String.format(
                  "Subscription already exists for tag - %s", channelSub.getChannel().getTitle()));
        });

    // Throws ResourceNotFoundException
    var channel = channelService.getByPermalink(channelSub.getChannel().getPermalink());
    var scope = scopeService.getByName(channelSub.getScope().getName());
    var type = typeService.getByName(channelSub.getType().getName());
    var order =
        orderService.getByValueAndType(
            channelSub.getOrder().getValue(),
            channelSub.getOrder().getSubscriptionType().getType());

    var optionalChat =
        telegramChatService.getByTelegramChatIdAndMessageThreadId(
            channelSub.getChat().getChatId(), channelSub.getChat().getMessageThreadId());

    if (optionalChat.isPresent()) {

      var chat = optionalChat.get();

      var newTagSubscription =
          ChannelSub.builder()
              .id(0)
              .channel(channel)
              .chat(chat)
              .chatUuid(channelSub.getChatUuid())
              .order(order)
              .scope(scope)
              .type(type)
              .lastPermalink(null)
              .build();

      return channelSubRepository.save(newTagSubscription);

    } else {

      var createdChat = telegramChatService.save(channelSub.getChat());

      var newTagSubscription =
          ChannelSub.builder()
              .id(0)
              .channel(channel)
              .chat(createdChat)
              .chatUuid(channelSub.getChatUuid())
              .order(order)
              .scope(scope)
              .type(type)
              .lastPermalink(null)
              .build();

      return channelSubRepository.save(newTagSubscription);
    }
  }

  @Transactional
  @Override
  public void deleteSubscription(ChannelSub channelSub) {

    var optional =
        channelSubRepository.getByChatChatIdAndChatMessageThreadIdAndChannelPermalinkAndOrderValue(
            channelSub.getChat().getChatId(),
            channelSub.getChat().getMessageThreadId(),
            channelSub.getChannel().getPermalink(),
            channelSub.getOrder().getValue());

    optional.ifPresent(channelSubRepository::delete);
  }
}
