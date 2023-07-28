package ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository;

import feign.Param;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.TagSubscription;

public interface TagSubscriptionRepository extends
    JpaRepository<TagSubscription, Long> {


  @Query(
      """
          select s, ch, sect, c from Subscription s
          join s.community c
          join s.chat ch
          join s.section sect
              """
  )
  @Override
  List<TagSubscription> findAll();


  @Query(
      """
          select ts, ch, t from TagSubscription ts
          join ts.chat ch
          join ts.tag t
          where ch.active = :active
              """
  )
  List<TagSubscription> findAllWithActiveChats(@Param("active") boolean active);

  @Query(
      """
          select ts, ch, t from TagSubscription ts
          join ts.chat ch
          join ts.tag t
          where ch.chatId = :telegramChatId
              """
  )
  List<TagSubscription> getAllByChatChatId(@Param("telegramChatId") long telegramChatId);


  @Query(
      """
          select ts, ch, t from TagSubscription ts
          join ts.chat ch
          join ts.tag t
          where t.title = :tagTitle
          and ch.chatId = :externalChatId
              """
  )
  Optional<TagSubscription> getByChatChatIdAndTagTitle(
      @Param("externalChatId") long externalChatId,
      @Param("tagTitle") String tagTitle);

}
