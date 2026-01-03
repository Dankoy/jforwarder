package ru.dankoy.subscriptionsholder.subscriptions_holder.core.controller.subsctiption.channel;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.controller.subscription.channel.ChannelSubController;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.channelsub.ChannelSubscriptionCreateDTO;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.channelsub.ChannelSubscriptionDTO;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.channel.ChannelMaker;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.subscription.channel.ChannelSubMaker;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.subscription.channel.ChannelSubService;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.telegramchat.ChatMaker;
import tools.jackson.databind.json.JsonMapper;

@DisplayName("ChannelSubController test ")
@WebMvcTest(controllers = ChannelSubController.class)
@AutoConfigureMockMvc
@Import({JsonMapper.class})
class ChannelSubControllerTest implements ChannelMaker, ChatMaker, ChannelSubMaker {

  @Autowired private MockMvc mvc;

  @Autowired private JsonMapper objectMapper;

  @MockitoBean private ChannelSubService channelSubService;

  @DisplayName("create should correctly create")
  @Test
  void createTest_shouldCorrectlyCreate() throws Exception {

    var channel = makeCorrectChannel();
    var chat = makeChat(123L);
    var inputSub = makeChannelSubs(channel, chat).getFirst();
    var outputSub = makeChannelSubs(channel, chat).getFirst();
    outputSub.setCreatedAt(LocalDateTime.now(ZoneOffset.UTC));
    outputSub.setId(1L);

    var inputDto = ChannelSubscriptionCreateDTO.toDTO(inputSub);
    var outputDto = ChannelSubscriptionDTO.toDTO(outputSub);

    given(channelSubService.createSubscription(any())).willReturn(outputSub);

    mvc.perform(
            post("/api/v1/channel_subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(objectMapper.writeValueAsString(outputDto)));

    Mockito.verify(channelSubService, times(1)).createSubscription(any());
  }

  @DisplayName("create order incorrect subscription type should correctly create")
  @ParameterizedTest
  @NullSource
  @EmptySource
  @ValueSource(strings = {"blah"})
  void createTest_incorrectSubscriptionType_shouldThrowValidationException(String val)
      throws Exception {

    var channel = makeCorrectChannel();
    var chat = makeChat(123L);
    var inputSub = makeChannelSubs(channel, chat).getFirst();
    inputSub.getOrder().getSubscriptionType().setType(val);

    var inputDto = ChannelSubscriptionCreateDTO.toDTO(inputSub);

    mvc.perform(
            post("/api/v1/channel_subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    Mockito.verify(channelSubService, times(0)).createSubscription(any());
  }
}
