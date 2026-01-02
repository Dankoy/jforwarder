package ru.dankoy.subscriptionsholder.subscriptions_holder.core.controller.channel;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
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
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.ChannelCreateDTO;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.channelsub.ChannelDTO;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceConflictException;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceNotFoundException;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.channel.ChannelMaker;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.channel.ChannelService;

@DisplayName("ChannelController test ")
@WebMvcTest(controllers = ChannelController.class)
@AutoConfigureMockMvc
@Import({ObjectMapper.class})
class ChannelControllerTest implements ChannelMaker {

  @Autowired private MockMvc mvc;

  @Autowired private ObjectMapper objectMapper;

  @MockitoBean private ChannelService channelService;

  @DisplayName("getByPermalink should return existing channel")
  @Test
  void getByPermalinkTest_shouldReturnCorrectChannel() throws Exception {

    var permalink = "tag1";

    var channel = makeCorrectChannel();
    var varCorrectChannelDto =
        new ChannelDTO(channel.getId(), channel.getTitle(), channel.getPermalink());

    given(channelService.getByPermalink(permalink)).willReturn(channel);

    mvc.perform(
            get("/api/v1/channels")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("permalink", permalink))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(objectMapper.writeValueAsString(varCorrectChannelDto)));

    Mockito.verify(channelService, times(1)).getByPermalink(permalink);
  }

  @DisplayName("getByPermalink should throw ResourceNotFoundException")
  @Test
  void getByPermalinkTest_shouldThrowResourceNotFoundException() throws Exception {

    var permalink = "tag1";

    given(channelService.getByPermalink(permalink)).willThrow(ResourceNotFoundException.class);

    mvc.perform(
            get("/api/v1/channels")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("permalink", permalink))
        .andExpect(status().isNotFound());

    Mockito.verify(channelService, times(1)).getByPermalink(permalink);
  }

  @DisplayName("create should create")
  @Test
  void createTest_shouldCorrectlyCreateChannel() throws Exception {

    var channel = makeCorrectChannel();
    var correctTagCreateDto = new ChannelCreateDTO(channel.getTitle(), channel.getPermalink());

    var varCorrectTagDto =
        new ChannelDTO(channel.getId(), channel.getTitle(), channel.getPermalink());

    given(channelService.create(channel)).willReturn(channel);

    mvc.perform(
            post("/api/v1/channels")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(correctTagCreateDto)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(objectMapper.writeValueAsString(varCorrectTagDto)));

    Mockito.verify(channelService, times(1)).create(channel);
  }

  @DisplayName("create should throw ResourceConflictException")
  @Test
  void createTest_shouldThrowResourceConflictException() throws Exception {

    var channel = makeCorrectChannel();
    var correctTagCreateDto = new ChannelCreateDTO(channel.getTitle(), channel.getPermalink());

    given(channelService.create(channel)).willThrow(ResourceConflictException.class);

    mvc.perform(
            post("/api/v1/channels")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(correctTagCreateDto)))
        .andExpect(status().isConflict())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    Mockito.verify(channelService, times(1)).create(channel);
  }

  @DisplayName("create should throw ValidationException")
  @ParameterizedTest
  @ValueSource(strings = {""})
  @NullSource
  void createTest_shouldThrowValidationException(String param) throws Exception {

    var correctTagCreateDto = new ChannelCreateDTO(param, param);

    mvc.perform(
            post("/api/v1/channels")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(correctTagCreateDto)))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    Mockito.verify(channelService, times(0)).create(any());
  }

  @DisplayName("delete should correctly delete")
  @Test
  void deleteTest_shouldCorrectlyDelete() throws Exception {

    var permalink = "tag1";

    mvc.perform(
            delete("/api/v1/channels/" + permalink)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    Mockito.verify(channelService, times(1)).deleteByTitle(permalink);
  }
}
