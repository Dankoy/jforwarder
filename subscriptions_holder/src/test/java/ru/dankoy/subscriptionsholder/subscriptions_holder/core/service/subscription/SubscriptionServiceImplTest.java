package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.subscription;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.Subscription;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceNotFoundException;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.TestContainerBase;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.channel.ChannelMaker;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.community.CommunityMaker;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.subscription.channel.ChannelSubMaker;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.subscription.community.CommunitySubMaker;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.telegramchat.ChatMaker;

@DisplayName("Test SubscriptionServiceImpl ")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({SubscriptionServiceImpl.class})
class SubscriptionServiceImplTest extends TestContainerBase
    implements CommunitySubMaker, ChannelSubMaker, CommunityMaker, ChatMaker, ChannelMaker {

  @Autowired private SubscriptionServiceImpl subscriptionService;

  @PersistenceContext private EntityManager entityManager;

  @DisplayName("findById expects correct response")
  @Test
  void findByIdTest_existingSub_expectsCorrectResponse() {

    var channel = makeCorrectChannel();
    var community = findCorrectCommunityByNameAndSectionName("memes", "weekly");
    var chat = makeChat(123L);
    entityManager.persist(chat);
    entityManager.persist(channel);
    entityManager.flush();

    var subs = makeCorrectCommunitySubs(community, chat);
    var sub = makeChannelSubs(channel, chat).getFirst();

    subs.forEach(entityManager::persist);
    entityManager.persist(sub);
    entityManager.flush();

    var actual1 = subscriptionService.findById(sub.getId());
    var actual2 = subscriptionService.findById(subs.getFirst().getId());

    var expected1 = entityManager.find(Subscription.class, sub.getId());
    var expected2 = entityManager.find(Subscription.class, subs.getFirst().getId());

    assertThat(actual1).isEqualTo(expected1);
    assertThat(actual2).isEqualTo(expected2);
  }

  @DisplayName("findById expects ResourceNotFoundException")
  @Test
  void findByIdTest_nonExistingSub_expectsResourceNotFoundException() {

    assertThatThrownBy(() -> subscriptionService.findById(1L))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @DisplayName("updatePermalink expects correct response")
  @Test
  void updatePermalinkTest_expectsCorrectResponse() {

    var newPermalink = "new";

    var channel = makeCorrectChannel();
    var chat = makeChat(123L);
    entityManager.persist(chat);
    entityManager.persist(channel);
    entityManager.flush();

    var sub = makeChannelSubs(channel, chat).getFirst();

    entityManager.persist(sub);
    entityManager.flush();

    var subWithNewPermalink =
        Subscription.builder().id(sub.getId()).lastPermalink(newPermalink).build();

    var actual = subscriptionService.updatePermalink(subWithNewPermalink);

    var expected = entityManager.find(Subscription.class, sub.getId());

    assertThat(actual).isEqualTo(expected);
  }

  @DisplayName("updatePermalink expects ResourceNotFoundException")
  @Test
  void updatePermalinkTest_expectsResourceNotFoundException() {

    var subWithNewPermalink = Subscription.builder().id(1L).lastPermalink("k").build();

    assertThatThrownBy(() -> subscriptionService.updatePermalink(subWithNewPermalink))
        .isInstanceOf(ResourceNotFoundException.class);
  }
}
