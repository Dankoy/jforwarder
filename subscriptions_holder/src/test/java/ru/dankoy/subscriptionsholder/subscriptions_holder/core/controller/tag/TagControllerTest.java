package ru.dankoy.subscriptionsholder.subscriptions_holder.core.controller.tag;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.tagsubscription.TagCreateDTO;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.tagsubscription.TagDTO;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceConflictException;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceNotFoundException;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.tag.TagMaker;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.tag.TagServiceImpl;

@DisplayName("TagController test ")
@WebMvcTest(controllers = TagController.class)
@AutoConfigureMockMvc
@Import({ObjectMapper.class})
class TagControllerTest implements TagMaker {

  @Autowired private MockMvc mvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private TagServiceImpl tagService;

  @DisplayName("getByTitle should correctly get by title")
  @Test
  void getByTitle_expectsCorrectResponse() throws Exception {

    var title = "tag1";

    var tag = makeCorrectTag(title);
    var varCorrectTagDto = new TagDTO(tag.getId(), tag.getTitle());

    given(tagService.getByTitle(title)).willReturn(tag);

    mvc.perform(
            get("/api/v1/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("title", title))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(objectMapper.writeValueAsString(varCorrectTagDto)));

    Mockito.verify(tagService, times(1)).getByTitle(title);
  }

  @DisplayName("getByTitle should throw ResourceNotFoundException")
  @Test
  void getByTitleTest_expectsNotFoundException() throws Exception {

    var title = "tag1";

    given(tagService.getByTitle(title)).willThrow(ResourceNotFoundException.class);

    mvc.perform(
            get("/api/v1/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("title", title))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    Mockito.verify(tagService, times(1)).getByTitle(title);
  }

  @DisplayName("create should create")
  @Test
  void create_expectsCorrectResponse() throws Exception {

    var title = "tag1";

    var tag = makeCorrectTag(title);
    var correctTagCreateDto = new TagCreateDTO(title);
    var varCorrectTagDto = new TagDTO(tag.getId(), tag.getTitle());

    given(tagService.create(tag)).willReturn(tag);

    mvc.perform(
            post("/api/v1/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(correctTagCreateDto)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(objectMapper.writeValueAsString(varCorrectTagDto)));

    Mockito.verify(tagService, times(1)).create(tag);
  }

  @DisplayName("create should throw ResourceConflictException")
  @Test
  void create_expectsResourceConflictException() throws Exception {

    var title = "tag1";

    var tag = makeCorrectTag(title);
    var correctTagCreateDto = new TagCreateDTO(title);

    given(tagService.create(tag)).willThrow(ResourceConflictException.class);

    mvc.perform(
            post("/api/v1/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(correctTagCreateDto)))
        .andExpect(status().isConflict())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    Mockito.verify(tagService, times(1)).create(tag);
  }

  @DisplayName("create should throw ValidationException")
  @Test
  void create_expectsVal() throws Exception {

    var title = "tag1";

    var tag = makeCorrectTag(title);
    var incorrectTagDto = new TagDTO(0L, null);

    mvc.perform(
            post("/api/v1/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(incorrectTagDto)))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    Mockito.verify(tagService, times(0)).create(tag);
  }

  @DisplayName("delete should return correct")
  @Test
  void delete_expectsCorrectResponse() throws Exception {

    var title = "tag1";

    mvc.perform(
            delete("/api/v1/tags/" + title)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    Mockito.verify(tagService, times(1)).deleteByTitle(title);
  }
}
