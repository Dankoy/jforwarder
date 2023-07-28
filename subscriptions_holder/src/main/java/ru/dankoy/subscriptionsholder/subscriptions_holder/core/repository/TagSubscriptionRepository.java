package ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.tag.TagSubscription;

public interface TagSubscriptionRepository extends
    JpaRepository<TagSubscription, Long> {


  @Query(
      """
          select ts, ch, ord, sco, tg, ty from TagSubscription ts
          join ts.chat ch
          join ts.tag tg
          join ts.order ord
          join ts.scope sco
          join ts.type ty
              """
  )
  @Override
  List<TagSubscription> findAll();


  @Query(
      """
          select ts, ch, t, ord, sco, ty from TagSubscription ts
          join ts.chat ch
          join ts.tag t
          join ts.order ord
          join ts.scope sco
          join ts.type ty
          where ch.active = :active
              """
  )
  List<TagSubscription> findAllWithActiveChats(@Param("active") boolean active);

  @Query(
      """
          select ts, ch, t, ord, sco, ty from TagSubscription ts
          join ts.chat ch
          join ts.tag t
          join ts.order ord
          join ts.scope sco
          join ts.type ty
          where ch.chatId = :telegramChatId
              """
  )
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
              """
  )
  List<TagSubscription> getAllByChatChatIdAndTagTitle(
      @Param("externalChatId") long externalChatId,
      @Param("tagTitle") String tagTitle);

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
              """
  )
  Optional<TagSubscription> getByChatChatIdAndTagTitleAndOrderName(
      @Param("externalChatId") long externalChatId,
      @Param("tagTitle") String tagTitle,
      @Param("orderName") String orderName);

}
