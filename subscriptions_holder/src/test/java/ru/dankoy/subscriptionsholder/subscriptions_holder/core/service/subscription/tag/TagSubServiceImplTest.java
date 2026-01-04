package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.subscription.tag;

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
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.TagSub;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceConflictException;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceNotFoundException;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.TestContainerBase;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.order.OrderServiceImpl;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.scope.ScopeServiceImpl;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.subscription.channel.ChannelSubMaker;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.tag.TagMaker;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.tag.TagServiceImpl;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.telegramchat.ChatMaker;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.telegramchat.TelegramChatServiceImpl;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.type.TypeServiceImpl;

@DisplayName("Test TagSubServiceImpl ")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({
  TagSubServiceImpl.class,
  TagServiceImpl.class,
  ScopeServiceImpl.class,
  OrderServiceImpl.class,
  TypeServiceImpl.class,
  TelegramChatServiceImpl.class
})
class TagSubServiceImplTest extends TestContainerBase
    implements ChatMaker, ChannelSubMaker, TagMaker, TagSubMaker {

  @Autowired private TagSubServiceImpl tagSubService;

  @PersistenceContext private EntityManager entityManager;

  private static final Pageable PAGEABLE = PageRequest.of(0, 10);

  @DisplayName("getAllByActiveTelegramChats active expects all")
  @Test
  void getAllByActiveTelegramChatsTest_active_expectsAllActive() {

    var tag = makeCorrectTag("tag1");
    var chat = makeChat(123L);
    entityManager.persist(chat);
    entityManager.persist(tag);
    entityManager.flush();

    var subs = makeTagSubs(tag, chat);

    subs.forEach(entityManager::persist);
    entityManager.flush();

    Page<TagSub> page = tagSubService.getAllByActiveTelegramChats(true, PAGEABLE);

    assertThat(page.getContent()).containsExactlyInAnyOrderElementsOf(subs);
  }

  @DisplayName("getAllByActiveTelegramChats active expects none")
  @Test
  void getAllByActiveTelegramChats_notActive_expectsEmptyList() {

    var tag = makeCorrectTag("tag1");
    var chat = makeChat(123L);
    entityManager.persist(chat);
    entityManager.persist(tag);
    entityManager.flush();

    var subs = makeTagSubs(tag, chat);

    subs.forEach(entityManager::persist);
    entityManager.flush();

    Page<TagSub> page = tagSubService.getAllByActiveTelegramChats(false, PAGEABLE);

    assertThat(page.getContent()).isEmpty();
  }

  @DisplayName("getAllByTelegramChatId existing chat expects correct response")
  @Test
  void getAllByTelegramChatId_existingChat_expectsCorrectList() {

    var tag = makeCorrectTag("tag1");
    var chat = makeChat(123L);
    entityManager.persist(chat);
    entityManager.persist(tag);
    entityManager.flush();

    var subs = makeTagSubs(tag, chat);

    subs.forEach(entityManager::persist);
    entityManager.flush();

    List<TagSub> page = tagSubService.getAllByTelegramChatId(chat.getChatId());

    assertThat(page).containsExactlyInAnyOrderElementsOf(subs);
  }

  @DisplayName("getAllByTelegramChatId non existing chat expects empty list")
  @Test
  void getAllByTelegramChatId_nonExistingChat_expectsEmptyList() {

    var tag = makeCorrectTag("tag1");
    var chat = makeChat(123L);
    entityManager.persist(chat);
    entityManager.persist(tag);
    entityManager.flush();

    var subs = makeTagSubs(tag, chat);

    subs.forEach(entityManager::persist);
    entityManager.flush();

    List<TagSub> page = tagSubService.getAllByTelegramChatId(66534L);

    assertThat(page).isEmpty();
  }

  @DisplayName("createSubscription chat already exists expects correct response")
  @Test
  void createSubscription_chatAlreadyExists_expectsCorrectResponse() {

    var tag = makeCorrectTag("tag1");
    var chat = makeChat(123L);
    entityManager.persist(chat);
    entityManager.persist(tag);
    entityManager.flush();

    var sub = makeTagSubs(tag, chat).getFirst();

    var expected = tagSubService.createSubscription(sub);

    var actual = entityManager.find(TagSub.class, expected.getId());

    assertThat(expected).isEqualTo(actual);
  }

  @DisplayName("createSubscription chat already exists expects correct response")
  @Test
  void createSubscription_chatNotExists_expectsChatCreationAndCorrectResponse() {

    var tag = makeCorrectTag("tag1");
    var chat = makeChat(123L);
    entityManager.persist(tag);
    entityManager.flush();

    var sub = makeTagSubs(tag, chat).getFirst();

    var expected = tagSubService.createSubscription(sub);

    var actual = entityManager.find(TagSub.class, expected.getId());

    assertThat(expected).isEqualTo(actual);
  }

  @DisplayName("createSubscription sub already exists expects ResourceConflictException")
  @Test
  void createSubscription_subAlreadyExists_expectsResourceConflictException() {

    var tag = makeCorrectTag("tag1");
    var chat = makeChat(123L);
    entityManager.persist(chat);
    entityManager.persist(tag);
    entityManager.flush();

    var sub = makeTagSubs(tag, chat).getFirst();
    entityManager.persist(sub);

    assertThatThrownBy(() -> tagSubService.createSubscription(sub))
        .isInstanceOf(ResourceConflictException.class);
  }

  @DisplayName("createSubscription tag not exists expects ResourceNotFoundException")
  @Test
  void createSubscription_tagNotExists_expectsResourceNotFoundException() {

    var tag = makeCorrectTag("tag1");
    var chat = makeChat(123L);
    entityManager.persist(chat);
    entityManager.flush();

    var sub = makeTagSubs(tag, chat).getFirst();

    assertThatThrownBy(() -> tagSubService.createSubscription(sub))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @DisplayName("deleteSubscription delete existing expects correct response")
  @Test
  void deleteSubscription_deleteExisting_expectsCorrectResponse() {

    var tag = makeCorrectTag("tag1");
    var chat = makeChat(123L);
    entityManager.persist(chat);
    entityManager.persist(tag);
    entityManager.flush();

    var sub = makeTagSubs(tag, chat).getFirst();
    entityManager.persist(sub);

    tagSubService.deleteSubscription(sub);

    var expected = entityManager.find(TagSub.class, sub.getId());

    assertThat(expected).isNull();
  }

  @DisplayName("deleteSubscription delete existing expects correct response")
  @Test
  void deleteSubscriptionTest_deleteNotExisting_expectsCorrectResponse() {

    var tag = makeCorrectTag("tag1");
    var chat = makeChat(123L);
    entityManager.persist(chat);
    entityManager.persist(tag);
    entityManager.flush();

    var sub = makeTagSubs(tag, chat).getFirst();

    tagSubService.deleteSubscription(sub);

    var expected = entityManager.find(TagSub.class, sub.getId());

    assertThat(expected).isNull();
  }
}
