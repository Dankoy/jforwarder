package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.subscription.channel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.Chat;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.ChannelSub;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.channel.Channel;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceConflictException;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.TestContainerBase;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.channel.ChannelMaker;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.channel.ChannelServiceImpl;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.order.OrderServiceImpl;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.scope.ScopeServiceImpl;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.telegramchat.ChatMaker;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.telegramchat.TelegramChatServiceImpl;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.type.TypeServiceImpl;

@DisplayName("Test ChannelSubServiceImpl ")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({
  ChannelSubServiceImpl.class,
  ChannelServiceImpl.class,
  ScopeServiceImpl.class,
  OrderServiceImpl.class,
  TypeServiceImpl.class,
  TelegramChatServiceImpl.class
})
class ChannelSubServiceImplTest extends TestContainerBase
    implements ChannelMaker, ChatMaker, ChannelSubMaker {

  @Autowired private ChannelSubServiceImpl channelSubService;

  @PersistenceContext private EntityManager entityManager;

  private static final Pageable PAGEABLE = PageRequest.of(0, 10);

  @DisplayName("getAllByActiveTelegramChats all active expects correct response")
  @Test
  void getAllByActiveTelegramChatsTest_activeTrue_expectsCorrectResponse() {

    var channel = makeCorrectChannel();
    var chat = makeChat(123L);
    entityManager.persist(channel);
    entityManager.persist(chat);
    entityManager.flush();

    var subs = makeChannelSubs(channel, chat);

    subs.forEach(entityManager::persist);
    entityManager.flush();

    Page<ChannelSub> page = channelSubService.getAllByActiveTelegramChats(true, PAGEABLE);

    assertThat(page.getContent()).containsExactlyInAnyOrderElementsOf(subs);
  }

  @DisplayName("getAllByActiveTelegramChats all active expects correct response")
  @Test
  void getAllByActiveTelegramChatsTest_activeFalse_expectsCorrectResponse() {

    var channel = makeCorrectChannel();
    var chat = makeChat(123L);
    entityManager.persist(channel);
    entityManager.persist(chat);
    entityManager.flush();

    var subs = makeChannelSubs(channel, chat);

    subs.forEach(entityManager::persist);
    entityManager.flush();

    Page<ChannelSub> page = channelSubService.getAllByActiveTelegramChats(false, PAGEABLE);

    assertThat(page.getContent()).isEmpty();
  }

  @DisplayName("getAllByTelegramChatId existing chat expects correct response")
  @Test
  void getAllByTelegramChatIdTest_existingChat_ExpectsCorrectResponse() {

    var channel = makeCorrectChannel();
    var chat = makeChat(123L);
    entityManager.persist(channel);
    entityManager.persist(chat);
    entityManager.flush();

    var subs = makeChannelSubs(channel, chat);

    subs.forEach(entityManager::persist);
    entityManager.flush();

    List<ChannelSub> actual = channelSubService.getAllByTelegramChatId(123L);

    assertThat(actual).containsExactlyInAnyOrderElementsOf(subs);
  }

  @DisplayName("getAllByTelegramChatId non existing chat expects empty")
  @Test
  void getAllByTelegramChatIdTest_nonExistingChat_ExpectsEmptyList() {

    var channel = makeCorrectChannel();
    var chat = makeChat(123L);
    entityManager.persist(channel);
    entityManager.persist(chat);
    entityManager.flush();

    var subs = makeChannelSubs(channel, chat);

    subs.forEach(entityManager::persist);
    entityManager.flush();

    List<ChannelSub> actual = channelSubService.getAllByTelegramChatId(1234L);

    assertThat(actual).isEmpty();
  }

  @DisplayName("createSubscription expects correct response")
  @Test
  void createSubscriptionTest_expectsCorrectResponse() {

    var channel = makeCorrectChannel();
    var chat = makeChat(123L);
    entityManager.persist(channel);
    entityManager.persist(chat);
    entityManager.flush();

    var sub = makeChannelSubs(channel, chat).getFirst();

    var expected = channelSubService.createSubscription(sub);

    var actual = entityManager.find(ChannelSub.class, expected.getId());

    assertThat(expected).isEqualTo(actual);
  }

  @DisplayName("createSubscription expects ResourceConflictException")
  @Test
  void createSubscriptionTest_expectsResourceConflictException() {

    var channel = makeCorrectChannel();
    var chat = makeChat(123L);
    entityManager.persist(channel);
    entityManager.persist(chat);
    entityManager.flush();

    var sub = makeChannelSubs(channel, chat).getFirst();

    entityManager.persist(sub);

    assertThatThrownBy(() -> channelSubService.createSubscription(sub))
        .isInstanceOf(ResourceConflictException.class);
  }

  @DisplayName("createSubscription chat not exists expects correct response")
  @Test
  void createSubscriptionTest_additionallyCreateChat_expectsCorrect() {

    var channel = makeCorrectChannel();
    var chat = makeChat(123L);
    entityManager.persist(channel);
    entityManager.flush();

    var sub = makeChannelSubs(channel, chat).getFirst();

    var expected = channelSubService.createSubscription(sub);

    var actual = entityManager.find(ChannelSub.class, expected.getId());

    assertThat(expected).isEqualTo(actual);
  }

  @DisplayName("deleteSubscription delete existing expects correct response")
  @Test
  void deleteSubscriptionTest_deleteExisting_expectsCorrectResponse() {

    var channel = makeCorrectChannel();
    var chat = makeChat(123L);
    entityManager.persist(channel);
    entityManager.persist(chat);
    entityManager.flush();

    var sub = makeChannelSubs(channel, chat).getFirst();

    entityManager.persist(sub);

    channelSubService.deleteSubscription(sub);

    var actual = entityManager.find(ChannelSub.class, sub.getId());

    var checkChannel = entityManager.find(Channel.class, channel.getId());
    var checkChat = entityManager.find(Chat.class, chat.getId());

    assertThat(actual).isNull();
    assertThat(checkChannel).isEqualTo(channel);
    assertThat(checkChat).isEqualTo(chat);
  }

  @DisplayName("deleteSubscription expects correct response")
  @Test
  void deleteSubscriptionTest_deleteNonExisting_expectsCorrectResponse() {

    var channel = makeCorrectChannel();
    var chat = makeChat(123L);
    entityManager.persist(channel);
    entityManager.persist(chat);
    entityManager.flush();

    var sub = makeChannelSubs(channel, chat).getFirst();

    channelSubService.deleteSubscription(sub);

    var actual = entityManager.find(ChannelSub.class, sub.getId());

    assertThat(actual).isNull();
  }
}
