package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.channel.Channel;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceConflictException;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceNotFoundException;

@DisplayName("Test channel service ")
@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({ChannelServiceImpl.class})
class ChannelServiceImplTest {

  @Container
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

  // Not necessary if @TestContainers annotation is used with @Container
  @BeforeAll
  static void beforeAll() {
    postgres.start();
  }

  @AfterAll
  static void afterAll() {
    postgres.stop();
  }

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
  }

  @Autowired private ChannelServiceImpl channelService;

  @PersistenceContext private EntityManager entityManager;

  private static final String channelName = "channel1";
  private static final String permalink = "permalink";

  private final Channel toPersist = new Channel(0, channelName, permalink);

  @DisplayName("get by title expects not found exception")
  @Test
  void getByTitleTestThrowsResourceNotFoundException() {

    assertThatThrownBy(() -> channelService.getByTitle(channelName))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @DisplayName("get by title expects correct response")
  @Test
  void getByTitleTestExpectsCorrect() {

    var channelToPersist = persistChannel(toPersist);

    var actual = channelService.getByTitle(channelName);

    assertThat(actual).isEqualTo(channelToPersist);
  }

  @DisplayName("get by permalink expects not found exception")
  @Test
  void getByPermalinkTestThrowsResourceNotFoundException() {

    assertThatThrownBy(() -> channelService.getByPermalink("non existing"))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @DisplayName("get by permalink expects correct response")
  @Test
  void getByPermalinkTestExpectsCorrect() {

    var channel = persistChannel(toPersist);

    var actual = channelService.getByPermalink(permalink);

    assertThat(actual).isEqualTo(channel);
  }

  @DisplayName("create channel expects resource conflict exception")
  @Test
  void createTestExpectsResourceConflictException() {

    var channel = persistChannel(toPersist);

    assertThatThrownBy(() -> channelService.create(channel))
        .isInstanceOf(ResourceConflictException.class);
  }

  @DisplayName("create channel expects correct")
  @Test
  void createTestExpectsCorrectResponse() {

    var actual = channelService.create(toPersist);

    var expected = getChannel(actual);

    assertThat(actual).isEqualTo(expected).isEqualTo(toPersist);
  }

  @DisplayName("modify channel expects resource not found exception")
  @Test
  void modifyTestExpectsResourceConflictException() {

    assertThatThrownBy(() -> channelService.modify(toPersist))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @DisplayName("modify channel expects correct")
  @Test
  void modifyTestExpectsCorrectResponse() {

    var channel = persistChannel(toPersist);

    channel.setPermalink("new");

    var modified = channelService.modify(channel);

    var expected = getChannel(modified);

    assertThat(modified).isEqualTo(expected).isEqualTo(channel);
  }

  @DisplayName("delete channel by title expects correct")
  @Test
  void deleteByTitleTestExpectsCorrectResponse() {

    var channel = persistChannel(toPersist);

    channelService.deleteByTitle(channel.getTitle());

    assertThatThrownBy(() -> getChannel(channel)).isInstanceOf(EntityNotFoundException.class);
  }

  @DisplayName("delete channel by permalink expects correct")
  @Test
  void deleteByPermalinkTestExpectsCorrectResponse() {

    var channel = persistChannel(toPersist);

    channelService.deleteByPermalink(channel.getPermalink());

    assertThatThrownBy(() -> getChannel(channel)).isInstanceOf(EntityNotFoundException.class);
  }

  private Channel persistChannel(Channel channel) {
    entityManager.persist(channel);
    return channel;
  }

  private Channel getChannel(Channel channel) {

    return entityManager.getReference(Channel.class, channel.getId());
  }
}
