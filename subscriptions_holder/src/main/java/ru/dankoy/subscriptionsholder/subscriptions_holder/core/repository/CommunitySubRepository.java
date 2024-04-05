package ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.CommunitySub;

public interface CommunitySubRepository extends JpaRepository<CommunitySub, Long> {

  @Override
  @EntityGraph(value = "community-subscription-full-inherited", type = EntityGraphType.LOAD)
  Page<CommunitySub> findAll(Pageable pageable);

  @EntityGraph(value = "community-subscription-full-inherited", type = EntityGraphType.LOAD)
  Page<CommunitySub> findAllByChatActive(@Param("active") boolean active, Pageable pageable);

  @EntityGraph(value = "community-subscription-full-inherited", type = EntityGraphType.LOAD)
  List<CommunitySub> getAllByChatChatId(@Param("telegramChatId") long telegramChatId);

  @EntityGraph(value = "community-subscription-full-inherited", type = EntityGraphType.LOAD)
  List<CommunitySub> getAllByChatChatIdAndChatMessageThreadId(
      @Param("telegramChatId") long telegramChatId,
      @Param("messageThreadId") Integer messageThreadId);

  @EntityGraph(value = "community-subscription-full-inherited", type = EntityGraphType.LOAD)
  Optional<CommunitySub> getByChatChatIdAndCommunityNameAndSectionName(
      @Param("externalChatId") long externalChatId,
      @Param("communityName") String communityName,
      @Param("sectionName") String sectionName);

  @EntityGraph(value = "community-subscription-full-inherited", type = EntityGraphType.LOAD)
  Optional<CommunitySub> getByChatChatIdAndChatMessageThreadIdAndCommunityNameAndSectionName(
      @Param("externalChatId") long externalChatId,
      @Param("messageThreadId") Integer messageThreadId,
      @Param("communityName") String communityName,
      @Param("sectionName") String sectionName);
}
