package ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.TagSub;

public interface TagSubRepository extends JpaRepository<TagSub, Long> {

  @EntityGraph(value = "tag-subscription-full-inherited", type = EntityGraphType.LOAD)
  @Override
  Page<TagSub> findAll(Pageable pageable);

  @EntityGraph(value = "tag-subscription-full-inherited", type = EntityGraphType.LOAD)
  Page<TagSub> findAllByChatActive(@Param("active") boolean active, Pageable pageable);

  @EntityGraph(value = "tag-subscription-full-inherited", type = EntityGraphType.LOAD)
  List<TagSub> getAllByChatChatId(@Param("telegramChatId") long telegramChatId);

  @EntityGraph(value = "tag-subscription-full-inherited", type = EntityGraphType.LOAD)
  List<TagSub> getAllByChatChatIdAndChatMessageThreadId(
      @Param("telegramChatId") long telegramChatId,
      @Param("messageThreadId") Integer messageThreadId);

  @EntityGraph(value = "tag-subscription-full-inherited", type = EntityGraphType.LOAD)
  List<TagSub> getAllByChatChatIdAndTagTitle(
      @Param("externalChatId") long externalChatId, @Param("tagTitle") String tagTitle);

  @EntityGraph(value = "tag-subscription-full-inherited", type = EntityGraphType.LOAD)
  Optional<TagSub> getByChatChatIdAndTagTitleAndOrderValue(
      @Param("externalChatId") long externalChatId,
      @Param("tagTitle") String tagTitle,
      @Param("orderValue") String orderValue);

  @EntityGraph(value = "tag-subscription-full-inherited", type = EntityGraphType.LOAD)
  Optional<TagSub> getByChatChatIdAndChatMessageThreadIdAndTagTitleAndOrderValue(
      @Param("externalChatId") long externalChatId,
      @Param("messageThreadId") Integer messageThreadId,
      @Param("tagTitle") String tagTitle,
      @Param("orderValue") String orderValue);

  @EntityGraph(value = "tag-subscription-full-inherited", type = EntityGraphType.LOAD)
  Page<TagSub> findAllBychatUuidIsIn(
      @Param(value = "chatUuid") List<String> chatUuid, Pageable pageable);
}
