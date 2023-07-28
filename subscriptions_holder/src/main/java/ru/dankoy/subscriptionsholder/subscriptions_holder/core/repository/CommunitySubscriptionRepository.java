package ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository;

import feign.Param;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.CommunitySubscription;

public interface CommunitySubscriptionRepository extends
    JpaRepository<CommunitySubscription, Long> {


  @Query(
      """
          select s, ch, sect, c from CommunitySubscription s
          join s.community c
          join s.chat ch
          join s.section sect
              """
  )
  @Override
  List<CommunitySubscription> findAll();


  @Query(
      """
          select s, ch, sect, c from CommunitySubscription s
          join s.community c
          join s.chat ch
          join s.section sect
          where ch.active = :active
              """
  )
  List<CommunitySubscription> findAllWithActiveChats(@Param("active") boolean active);

  @Query(
      """
          select s, ch, sect, c from CommunitySubscription s
          join s.community c
          join s.chat ch
          join s.section sect
          where c.name = :communityName
              """
  )
  List<CommunitySubscription> getAllByCommunityName(@Param("communityName") String communityName);


  @Query(
      """
          select s, ch, sect, c from CommunitySubscription s
          join s.community c
          join s.chat ch
          join s.section sect
          where ch.chatId = :telegramChatId
              """
  )
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
              """
  )
  Optional<CommunitySubscription> getByChatChatIdAndCommunityNameAndSectionName(
      @Param("externalChatId") long externalChatId,
      @Param("communityName") String communityName,
      @Param("sectionName") String sectionName);

}
