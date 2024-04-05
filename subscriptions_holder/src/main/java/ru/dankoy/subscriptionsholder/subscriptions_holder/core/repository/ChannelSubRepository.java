package ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.ChannelSub;

public interface ChannelSubRepository extends JpaRepository<ChannelSub, Long> {

  @EntityGraph(value = "channel-subscription-full-inherited", type = EntityGraphType.LOAD)
  @Override
  Page<ChannelSub> findAll(Pageable pageable);

  @EntityGraph(value = "channel-subscription-full-inherited", type = EntityGraphType.LOAD)
  Page<ChannelSub> findAllByChatActive(@Param("active") boolean active, Pageable pageable);

  @EntityGraph(value = "channel-subscription-full-inherited", type = EntityGraphType.LOAD)
  List<ChannelSub> getAllByChatChatId(@Param("telegramChatId") long telegramChatId);

  @EntityGraph(value = "channel-subscription-full-inherited", type = EntityGraphType.LOAD)
  List<ChannelSub> getAllByChatChatIdAndChatMessageThreadId(
      @Param("telegramChatId") long telegramChatId,
      @Param("messageThreadId") Integer messageThreadId);

  @EntityGraph(value = "channel-subscription-full-inherited", type = EntityGraphType.LOAD)
  Optional<ChannelSub> getByChatChatIdAndChannelPermalinkAndOrderValue(
      @Param("externalChatId") long externalChatId,
      @Param("channelPermalink") String channelPermalink,
      @Param("orderValue") String orderValue);

  @EntityGraph(value = "channel-subscription-full-inherited", type = EntityGraphType.LOAD)
  Optional<ChannelSub> getByChatChatIdAndChatMessageThreadIdAndChannelPermalinkAndOrderValue(
      @Param("externalChatId") long externalChatId,
      @Param("messageThreadId") Integer messageThreadId,
      @Param("channelPermalink") String channelPermalink,
      @Param("orderValue") String orderValue);
}
