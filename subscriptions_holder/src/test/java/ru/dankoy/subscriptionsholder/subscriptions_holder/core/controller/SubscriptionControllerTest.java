package ru.dankoy.subscriptionsholder.subscriptions_holder.core.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.Subscription;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.SubscriptionUpdatePermalinkDTO;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.ChatMaker;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.CommunityMaker;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.CommunitySubMaker;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.SubscriptionService;

@DisplayName("SubscriptionController test ")
@WebMvcTest(controllers = SubscriptionController.class)
@AutoConfigureMockMvc
@Import({ObjectMapper.class})
class SubscriptionControllerTest implements CommunitySubMaker, ChatMaker, CommunityMaker {

  @Autowired private MockMvc mvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private SubscriptionService subscriptionService;

  @DisplayName("updatePermalink should correctly update")
  @Test
  void updatePermalinkTest_shouldCorrectlyUpdate() throws Exception {

    var s = Subscription.builder().id(1L).lastPermalink("lp").build();

    given(subscriptionService.updatePermalink(any())).willReturn(s);

    var dto = new SubscriptionUpdatePermalinkDTO(s.getId(), s.getLastPermalink());

    mvc.perform(
            put("/api/v1/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isAccepted())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(objectMapper.writeValueAsString(dto)));

    Mockito.verify(subscriptionService, times(1)).updatePermalink(s);
  }

  @DisplayName("updatePermalink id is not valid should throw ValidationException")
  @ParameterizedTest
  @ValueSource(ints = {0, -1, -100})
  void updatePermalinkTest_idIsNotValid_shouldThrowValidationException(int val) throws Exception {

    var s = Subscription.builder().id(val).lastPermalink("lp").build();

    given(subscriptionService.updatePermalink(any())).willReturn(s);

    var dto = new SubscriptionUpdatePermalinkDTO(s.getId(), s.getLastPermalink());

    mvc.perform(
            put("/api/v1/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    Mockito.verify(subscriptionService, times(0)).updatePermalink(s);
  }

  @DisplayName("updatePermalink permalink is not valid should throw ValidationException")
  @ParameterizedTest
  @NullSource
  @ValueSource(strings = {""})
  void updatePermalinkTest_permalinkIsNotValid_shouldThrowValidationException(String val)
      throws Exception {

    var s = Subscription.builder().id(1L).lastPermalink(val).build();

    given(subscriptionService.updatePermalink(any())).willReturn(s);

    var dto = new SubscriptionUpdatePermalinkDTO(s.getId(), s.getLastPermalink());

    mvc.perform(
            put("/api/v1/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    Mockito.verify(subscriptionService, times(0)).updatePermalink(s);
  }
}
