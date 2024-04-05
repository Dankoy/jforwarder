package ru.dankoy.telegrambot.core.service.subscription;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.dankoy.telegrambot.core.exceptions.NotFoundException;
import ru.dankoy.telegrambot.core.feign.subscriptionsholder.SubscriptionsHolderFeign;
import ru.dankoy.telegrambot.core.service.community.CommunityService;

@DisplayName("CommunitySubscriptionServiceImpl tests")
@ExtendWith(MockitoExtension.class)
class CommunitySubscriptionServiceImplTest implements CommunitySubMaker, CommunityMaker, ChatMaker {

  @Mock private SubscriptionsHolderFeign subscriptionsHolderFeign;

  @Mock private CommunityService communityService;

  @InjectMocks private CommunitySubscriptionServiceImpl communitySubscriptionService;

  @DisplayName("getSubscriptionsByChatId expects correct list")
  @Test
  void getSubscriptionsByChatIdTest() {

    var chatId = 123L;

    var community = correctCommunities().getFirst();
    var chat = makeChat(chatId);
    var subs = makeCorrectCommunitySubs(community, chat);

    given(subscriptionsHolderFeign.getAllSubscriptionsByChatId(chatId)).willReturn(subs);

    var expected = communitySubscriptionService.getSubscriptionsByChatId(chatId);

    assertThat(expected).isEqualTo(subs);
  }

  @DisplayName("subscribe expects correct response")
  @Test
  void subscribeTest_expectsCorrectResponse() {

    var chatId = 123L;
    var communityName = "memes";
    var sectionName = "daily";

    var community = correctCommunities().getFirst();
    var chat = makeChat(chatId);
    var sub = makeCorrectCommunitySubs(community, chat).getFirst();

    given(communityService.getByName(communityName)).willReturn(Optional.of(community));
    given(communitySubscriptionService.subscribe(communityName, sectionName, chatId))
        .willReturn(sub);

    var expected = communitySubscriptionService.subscribe(communityName, sectionName, chatId);

    verify(subscriptionsHolderFeign, times(1)).subscribe(any());
    assertThat(expected).isEqualTo(sub);
  }

  @DisplayName("subscribe community not exists expects NotFoundException")
  @Test
  void subscribeTest_communityNotFound_expectsNotFoundException() {

    var chatId = 123L;
    var communityName = "blah";
    var sectionName = "daily";

    given(communityService.getByName(communityName)).willReturn(Optional.empty());

    assertThatThrownBy(
            () -> communitySubscriptionService.subscribe(communityName, sectionName, chatId))
        .isInstanceOf(NotFoundException.class);

    verify(subscriptionsHolderFeign, times(0)).subscribe(any());
  }

  @DisplayName("subscribe section not exists expects NotFoundException")
  @Test
  void subscribeTest_sectionNotFound_expectsNotFoundException() {

    var chatId = 123L;
    var communityName = "memes";
    var sectionName = "blah";

    var community = correctCommunities().getFirst();

    given(communityService.getByName(communityName)).willReturn(Optional.of(community));

    assertThatThrownBy(
            () -> communitySubscriptionService.subscribe(communityName, sectionName, chatId))
        .isInstanceOf(NotFoundException.class);

    verify(subscriptionsHolderFeign, times(0)).subscribe(any());
  }

  @DisplayName("unsubscribe expects correct response")
  @Test
  void unsubscribe() {

    var chatId = 123L;
    var communityName = "memes";
    var sectionName = "blah";

    communitySubscriptionService.unsubscribe(communityName, sectionName, chatId);

    verify(subscriptionsHolderFeign, times(1)).unsubscribe(any());
  }
}
