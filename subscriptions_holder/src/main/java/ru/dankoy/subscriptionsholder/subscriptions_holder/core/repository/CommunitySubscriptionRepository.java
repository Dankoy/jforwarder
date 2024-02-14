package ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository;

import feign.Param;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.CommunitySubscription;

/**
 * @deprecated in favor for {@link CommunitySubRepository}
 */
@Deprecated(since = "2024-01-27")
public interface CommunitySubscriptionRepository
        extends JpaRepository<CommunitySubscription, Long> {

    // todo: Pageable with query returns ArrayList<Object[]> for whatever reason.
    //  Probably native query fix the problem.
    //  Decided to use named graphs
    //  @Query(
    //      """
    //          select s, ch, sect, c from CommunitySubscription s
    //          join s.community c
    //          join s.chat ch
    //          join s.section sect
    //              """
    //  )
    //  @EntityGraph(value = "findAllWithActiveChats")
    @Override
    @EntityGraph(value = "community-subscription-full", type = EntityGraphType.LOAD)
    Page<CommunitySubscription> findAll(Pageable pageable);

    //  @Query(
    //      """
    //          select s, ch, sect, c from CommunitySubscription s
    //          join s.community c
    //          join s.chat ch
    //          join s.section sect
    //          where ch.active = :active
    //              """
    //  )

    @EntityGraph(value = "community-subscription-full", type = EntityGraphType.LOAD)
    Page<CommunitySubscription> findAllByChatActive(
            @Param("active") boolean active, Pageable pageable);

    @Query(
            """
          select s, ch, sect, c from CommunitySubscription s
          join s.community c
          join s.chat ch
          join s.section sect
          where c.name = :communityName
              """)
    List<CommunitySubscription> getAllByCommunityName(@Param("communityName") String communityName);

    @Query(
            """
          select s, ch, sect, c from CommunitySubscription s
          join s.community c
          join s.chat ch
          join s.section sect
          where ch.chatId = :telegramChatId
              """)
    List<CommunitySubscription> getAllByChatChatId(@Param("telegramChatId") long telegramChatId);

    @Query(
            """
          select s, ch, sect, c from CommunitySubscription s
          join s.community c
          join s.chat ch
          join s.section sect
          where c.name = :communityName
          and ch.chatId = :externalChatId
          and sect.name = :sectionName
              """)
    Optional<CommunitySubscription> getByChatChatIdAndCommunityNameAndSectionName(
            @Param("externalChatId") long externalChatId,
            @Param("communityName") String communityName,
            @Param("sectionName") String sectionName);
}
