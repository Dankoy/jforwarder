package ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository;

import feign.Param;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.Subscription;

public interface SubscriptionRepository extends
    JpaRepository<Subscription, Long> {


  @Query(
      """
          select s, ch, sect, c from Subscription s
          join s.community c
          join s.chat ch
          join s.section sect
              """
  )
  @Override
  List<Subscription> findAll();

  @Query(
      """
          select s, ch, sect, c from Subscription s
          join s.community c
          join s.chat ch
          join s.section sect
          where c.name = :communityName
              """
  )
  List<Subscription> getAllByCommunityName(@Param("communityName") String communityName);


  @Query(
      """
          select s, ch, sect, c from Subscription s
          join s.community c
          join s.chat ch
          join s.section sect
          where ch.chatId = :telegramChatId
              """
  )
  List<Subscription> getAllByChatChatId(@Param("telegramChatId") long telegramChatId);


  @Query(
      """
          select s, ch, sect, c from Subscription s
          join s.community c
          join s.chat ch
          join s.section sect
          where c.name = :communityName
          and ch.chatId = :externalChatId
          and sect.name = :sectionName
              """
  )
  Optional<Subscription> getByChatChatIdAndCommunityNameAndSectionName(
      @Param("externalChatId") long externalChatId,
      @Param("communityName") String communityName,
      @Param("sectionName") String sectionName);

}
