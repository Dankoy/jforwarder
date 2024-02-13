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

  @EntityGraph(value = "tag-subscription-full-inherited", type = EntityGraphType.LOAD)
  @Override
  Page<ChannelSub> findAll(Pageable pageable);

  @EntityGraph(value = "tag-subscription-full-inherited", type = EntityGraphType.LOAD)
  Page<ChannelSub> findAllByChatActive(@Param("active") boolean active, Pageable pageable);

  @EntityGraph(value = "tag-subscription-full-inherited", type = EntityGraphType.LOAD)
  List<ChannelSub> getAllByChatChatId(@Param("telegramChatId") long telegramChatId);

  @EntityGraph(value = "tag-subscription-full-inherited", type = EntityGraphType.LOAD)
  List<ChannelSub> getAllByChatChatIdAndChannelTitle(
      @Param("externalChatId") long externalChatId, @Param("tagTitle") String tagTitle);

  @EntityGraph(value = "tag-subscription-full-inherited", type = EntityGraphType.LOAD)
  Optional<ChannelSub> getByChatChatIdAndChannelTitleAndOrderValue(
      @Param("externalChatId") long externalChatId,
      @Param("tagTitle") String tagTitle,
      @Param("orderValue") String orderValue);
}
