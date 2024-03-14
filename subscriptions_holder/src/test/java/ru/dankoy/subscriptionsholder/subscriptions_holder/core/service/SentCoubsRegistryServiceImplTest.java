package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.registry.SentCoubsRegistry;

@DisplayName("Test SentCoubsRegistryServiceImpl ")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({SentCoubsRegistryServiceImpl.class})
class SentCoubsRegistryServiceImplTest extends TestContainerBase
    implements SentCoubsRegistryMaker, ChannelSubMaker, ChannelMaker, ChatMaker {

  @Autowired private SentCoubsRegistryServiceImpl sentCoubsRegistryService;

  @PersistenceContext private EntityManager entityManager;

  private static final Pageable PAGEABLE = PageRequest.of(0, 10);

  @DisplayName("findAll expects correct response")
  @Test
  void findAllTest_expectsCorrectResponse() {

    var channel = makeCorrectChannel();
    var chat = makeChat(123L);
    entityManager.persist(channel);
    entityManager.persist(chat);
    entityManager.flush();

    var subs = makeChannelSubs(channel, chat);

    subs.forEach(entityManager::persist);
    entityManager.flush();

    var registry = makeCorrectRegistry(subs.getFirst());

    registry.forEach(entityManager::persist);

    Page<SentCoubsRegistry> page = sentCoubsRegistryService.findAll(PAGEABLE);

    assertThat(page.getContent()).containsExactlyInAnyOrderElementsOf(registry);
  }

  @DisplayName("findAll expects empty list")
  @Test
  void findAllTest_expectsEmptyList() {

    Page<SentCoubsRegistry> page = sentCoubsRegistryService.findAll(PAGEABLE);

    assertThat(page.getContent()).isEmpty();
  }

  @DisplayName("getAllBySubscriptionId expects correct response")
  @Test
  void getAllBySubscriptionIdTest_expectsCorrectResponse() {

    var channel = makeCorrectChannel();
    var chat = makeChat(123L);
    entityManager.persist(channel);
    entityManager.persist(chat);
    entityManager.flush();

    var subs = makeChannelSubs(channel, chat);

    subs.forEach(entityManager::persist);
    entityManager.flush();

    var registry = makeCorrectRegistry(subs.getFirst());

    registry.forEach(entityManager::persist);

    Page<SentCoubsRegistry> page =
        sentCoubsRegistryService.getAllBySubscriptionId(subs.getFirst().getId(), PAGEABLE);

    assertThat(page.getContent()).containsExactlyInAnyOrderElementsOf(registry);
  }

  @DisplayName("getAllBySubscriptionId expects empty list")
  @Test
  void getAllBySubscriptionIdTest_expectsEmptyList() {

    var channel = makeCorrectChannel();
    var chat = makeChat(123L);
    entityManager.persist(channel);
    entityManager.persist(chat);
    entityManager.flush();

    var subs = makeChannelSubs(channel, chat);

    subs.forEach(entityManager::persist);
    entityManager.flush();

    var registry = makeCorrectRegistry(subs.getFirst());

    registry.forEach(entityManager::persist);

    Page<SentCoubsRegistry> page =
        sentCoubsRegistryService.getAllBySubscriptionId(123345L, PAGEABLE);

    assertThat(page.getContent()).isEmpty();
  }

  @DisplayName("getAllBySubscriptionIdAndDateTimeAfter expects correct response")
  @Test
  void getAllBySubscriptionIdAndDateTimeAfterTest_expectsCorrectResponse() {

    var channel = makeCorrectChannel();
    var chat = makeChat(123L);
    entityManager.persist(channel);
    entityManager.persist(chat);
    entityManager.flush();

    var subs = makeChannelSubs(channel, chat);

    subs.forEach(entityManager::persist);
    entityManager.flush();

    var registry = makeCorrectRegistry(subs.getFirst());

    registry.forEach(entityManager::persist);

    var localDataTime = LocalDateTime.now().minusDays(1);

    Page<SentCoubsRegistry> page =
        sentCoubsRegistryService.getAllBySubscriptionIdAndDateTimeAfter(
            subs.getFirst().getId(), localDataTime, PAGEABLE);

    assertThat(page.getContent()).containsExactlyInAnyOrderElementsOf(registry);
  }

  @DisplayName("getAllBySubscriptionIdAndDateTimeAfter expects filtered by date")
  @Test
  void getAllBySubscriptionIdAndDateTimeAfterTest_expectsFilteredList() {

    var channel = makeCorrectChannel();
    var chat = makeChat(123L);
    entityManager.persist(channel);
    entityManager.persist(chat);
    entityManager.flush();

    var subs = makeChannelSubs(channel, chat);

    subs.forEach(entityManager::persist);
    entityManager.flush();

    var registry = makeCorrectRegistry(subs.getFirst());

    registry.forEach(entityManager::persist);

    var localDataTime = LocalDateTime.now().plusDays(1);

    Page<SentCoubsRegistry> page =
        sentCoubsRegistryService.getAllBySubscriptionIdAndDateTimeAfter(
            subs.getFirst().getId(), localDataTime, PAGEABLE);

    assertThat(page.getContent()).isEmpty();
  }

  @DisplayName("create expects correct response")
  @Test
  void createTest_expectsCorrect() {

    var channel = makeCorrectChannel();
    var chat = makeChat(123L);
    entityManager.persist(channel);
    entityManager.persist(chat);
    entityManager.flush();

    var subs = makeChannelSubs(channel, chat);

    subs.forEach(entityManager::persist);
    entityManager.flush();

    var registry = new ArrayList<>(makeCorrectRegistry(subs.getFirst()));

    var expected = sentCoubsRegistryService.create(registry.getFirst());

    var actual = entityManager.find(SentCoubsRegistry.class, expected.getId());

    assertThat(expected).isEqualTo(actual);
  }

  @DisplayName("create subscription not exists expects ConstraintViolationException")
  @Test
  void createTest_subscriptionNotExists_expectsConstraintViolationException() {

    var channel = makeCorrectChannel();
    var chat = makeChat(123L);
    entityManager.persist(chat);
    entityManager.flush();

    var subs = makeChannelSubs(channel, chat);
    subs.getFirst().setId(123234L);

    var registry = new ArrayList<>(makeCorrectRegistry(subs.getFirst())).getFirst();

    assertThatThrownBy(() -> sentCoubsRegistryService.create(registry))
        .isInstanceOf(DataIntegrityViolationException.class);
  }

  @DisplayName("deleteById existing expects correct deletion")
  @Test
  void deleteById_deletesExistingRegistry_expectsCorrectResponse() {

    var channel = makeCorrectChannel();
    var chat = makeChat(123L);
    entityManager.persist(channel);
    entityManager.persist(chat);
    entityManager.flush();

    var subs = makeChannelSubs(channel, chat);

    subs.forEach(entityManager::persist);
    entityManager.flush();

    var registry = new ArrayList<>(makeCorrectRegistry(subs.getFirst()));

    registry.forEach(entityManager::persist);

    sentCoubsRegistryService.deleteById(registry.getFirst().getId());

    var actual = entityManager.find(SentCoubsRegistry.class, registry.getFirst().getId());

    assertThat(actual).isNull();
  }

  @DisplayName("deleteById not existing expects correct response")
  @Test
  void deleteById_deletesNotExistingRegistry_expectsCorrectResponse() {

    var rId = 123422L;

    var channel = makeCorrectChannel();
    var chat = makeChat(123L);
    var subs = makeChannelSubs(channel, chat);
    var registry = new ArrayList<>(makeCorrectRegistry(subs.getFirst()));
    registry.getFirst().setId(rId);

    var empty = entityManager.find(SentCoubsRegistry.class, rId);
    assertThat(empty).isNull();

    sentCoubsRegistryService.deleteById(registry.getFirst().getId());

    var actual = entityManager.find(SentCoubsRegistry.class, rId);

    assertThat(actual).isNull();
  }
}
