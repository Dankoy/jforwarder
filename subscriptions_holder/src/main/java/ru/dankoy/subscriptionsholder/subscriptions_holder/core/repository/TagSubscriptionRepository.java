package ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.tag.TagSubscription;

/**
 * @deprecated in favor for {@link TagSubRepository}
 */
@Deprecated(since = "2024-01-27")
public interface TagSubscriptionRepository extends JpaRepository<TagSubscription, Long> {

  // todo: Pageable with query returns ArrayList<Object[]> for whatever reason.
  //  Probably native query fix the problem.
  //  Decided to use named graphs

  //  @Query(
  //      """
  //          select ts, ch, ord, sco, tg, ty from TagSubscription ts
  //          join ts.chat ch
  //          join ts.tag tg
  //          join ts.order ord
  //          join ts.scope sco
  //          join ts.type ty
  //              """
  //  )
  @EntityGraph(value = "tag-subscription-full", type = EntityGraphType.LOAD)
  @Override
  Page<TagSubscription> findAll(Pageable pageable);

  //  @Query(
  //      """
  //          select ts, ch, t, ord, sco, ty from TagSubscription ts
  //          join ts.chat ch
  //          join ts.tag t
  //          join ts.order ord
  //          join ts.scope sco
  //          join ts.type ty
  //          where ch.active = :active
  //              """
  //  )
  @EntityGraph(value = "tag-subscription-full", type = EntityGraphType.LOAD)
  Page<TagSubscription> findAllByChatActive(@Param("active") boolean active, Pageable pageable);

  @Query(
      """
          select ts, ch, t, ord, sco, ty from TagSubscription ts
          join ts.chat ch
          join ts.tag t
          join ts.order ord
          join ts.scope sco
          join ts.type ty
          where ch.chatId = :telegramChatId
              """)
  List<TagSubscription> getAllByChatChatId(@Param("telegramChatId") long telegramChatId);

  @Query(
      """
          select ts, ch, t, ord, sco, ty from TagSubscription ts
          join ts.chat ch
          join ts.tag t
          join ts.order ord
          join ts.scope sco
          join ts.type ty
          where t.title = :tagTitle
          and ch.chatId = :externalChatId
              """)
  List<TagSubscription> getAllByChatChatIdAndTagTitle(
      @Param("externalChatId") long externalChatId, @Param("tagTitle") String tagTitle);

  @Query(
      """
          select ts, ch, t, ord, sco, ty from TagSubscription ts
          join ts.chat ch
          join ts.tag t
          join ts.order ord
          join ts.scope sco
          join ts.type ty
          where t.title = :tagTitle
          and ch.chatId = :externalChatId
          and ord.name = :orderName
              """)
  Optional<TagSubscription> getByChatChatIdAndTagTitleAndOrderName(
      @Param("externalChatId") long externalChatId,
      @Param("tagTitle") String tagTitle,
      @Param("orderName") String orderName);
}
