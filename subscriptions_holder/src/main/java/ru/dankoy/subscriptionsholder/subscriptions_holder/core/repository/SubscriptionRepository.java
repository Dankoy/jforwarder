package ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.CommunityTelegramChatPK;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.Subscription;

public interface SubscriptionRepository extends
    JpaRepository<Subscription, CommunityTelegramChatPK> {

  List<Subscription> getAllByCommunityChatCommunityName(String communityName);

  List<Subscription> getAllByCommunityChatTelegramChatId(long telegramChatId);

  Optional<Subscription> getSubscriptionByCommunityChatTelegramChatChatIdAndCommunityChatCommunityNameAndCommunityChatCommunitySectionName(
      long externalChatId, String communityName, String sectionName);

  Optional<Subscription> getSubscriptionByCommunityChatTelegramChatIdAndCommunityChatCommunityNameAndCommunityChatCommunitySectionName(
      long chatId, String communityName, String sectionName);

}
