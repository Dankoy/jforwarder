package ru.dankoy.telegrambot.core.service.channel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import feign.FeignException.NotFound;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.dankoy.telegrambot.core.feign.subscriptionsholder.SubscriptionsHolderFeign;

@DisplayName("ChannelServiceImpl test ")
@ExtendWith(MockitoExtension.class)
class ChannelServiceImplTest implements ChannelMaker {

  @Mock(name = "subscriptionsHolderFeign")
  private SubscriptionsHolderFeign subscriptionsHolderFeign;

  @InjectMocks private ChannelServiceImpl channelService;

  @DisplayName("findChannelByPermalink expects correct")
  @Test
  void findChannelByPermalinkTest_expectsCorrect() {

    var permalink = "permalink";

    var correctChannel = makeCorrectChannel();

    given(subscriptionsHolderFeign.getChannelByPermalink(permalink)).willReturn(correctChannel);

    var optionalChannel = channelService.findChannelByPermalink(permalink);

    assertThat(optionalChannel).isPresent().contains(correctChannel);
  }

  @DisplayName("findChannelByPermalink expects empty")
  @Test
  void findChannelByPermalinkTest_expectsEmpty() {

    var permalink = "permalink";

    given(subscriptionsHolderFeign.getChannelByPermalink(permalink)).willThrow(NotFound.class);

    var optionalChannel = channelService.findChannelByPermalink(permalink);

    assertThat(optionalChannel).isEmpty();
  }
}
