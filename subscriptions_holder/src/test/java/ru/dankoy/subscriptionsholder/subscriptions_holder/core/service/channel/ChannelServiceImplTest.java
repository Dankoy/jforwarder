package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.channel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.channel.Channel;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceConflictException;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceNotFoundException;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.TestContainerBase;

@DisplayName("Test channel service ")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({ChannelServiceImpl.class})
class ChannelServiceImplTest extends TestContainerBase implements ChannelMaker {

  @Autowired private ChannelServiceImpl channelService;

  @PersistenceContext private EntityManager entityManager;

  private static final String channelName = "channel1";
  private static final String permalink = "permalink";

  @DisplayName("get by title expects not found exception")
  @Test
  void getByTitleTestThrowsResourceNotFoundException() {

    assertThatThrownBy(() -> channelService.getByTitle(channelName))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @DisplayName("get by title expects correct response")
  @Test
  void getByTitleTestExpectsCorrect() {

    var channelToPersist = persistChannel(makeCorrectChannel());

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

    var channel = persistChannel(makeCorrectChannel());

    var actual = channelService.getByPermalink(permalink);

    assertThat(actual).isEqualTo(channel);
  }

  @DisplayName("create channel expects resource conflict exception")
  @Test
  void createTestExpectsResourceConflictException() {

    var channel = persistChannel(makeCorrectChannel());

    assertThatThrownBy(() -> channelService.create(channel))
        .isInstanceOf(ResourceConflictException.class);
  }

  @DisplayName("create channel expects correct")
  @Test
  void createTestExpectsCorrectResponse() {

    var actual = channelService.create(makeCorrectChannel());

    var expected = getChannel(actual);

    assertThat(actual).isEqualTo(expected);
  }

  @DisplayName("modify channel expects resource not found exception")
  @Test
  void modifyTestExpectsResourceConflictException() {

    var toPersist = makeCorrectChannel();

    assertThatThrownBy(() -> channelService.modify(toPersist))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @DisplayName("modify channel expects correct")
  @Test
  void modifyTestExpectsCorrectResponse() {

    var channel = persistChannel(makeCorrectChannel());

    channel.setPermalink("new");

    var modified = channelService.modify(channel);

    var expected = getChannel(modified);

    assertThat(modified).isEqualTo(expected).isEqualTo(channel);
  }

  @DisplayName("delete channel by title expects correct")
  @Test
  void deleteByTitleTestExpectsCorrectResponse() {

    var channel = persistChannel(makeCorrectChannel());

    channelService.deleteByTitle(channel.getTitle());

    assertThatThrownBy(() -> getChannel(channel)).isInstanceOf(EntityNotFoundException.class);
  }

  @DisplayName("delete channel by permalink expects correct")
  @Test
  void deleteByPermalinkTestExpectsCorrectResponse() {

    var channel = persistChannel(makeCorrectChannel());

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
