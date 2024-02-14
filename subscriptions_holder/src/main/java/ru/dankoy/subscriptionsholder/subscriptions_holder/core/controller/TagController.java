package ru.dankoy.subscriptionsholder.subscriptions_holder.core.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.tagsubscription.TagCreateDTO;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.tagsubscription.TagDTO;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.TagService;

@RequiredArgsConstructor
@RestController
public class TagController {

  private final TagService tagService;

  @GetMapping(
      value = "/api/v1/tags",
      params = {"title"})
  public TagDTO getByTitle(@RequestParam("title") String title) {

    var found = tagService.getByTitle(title);

    return TagDTO.toDTO(found);
  }

  @PostMapping(value = "/api/v1/tags")
  public TagDTO create(@Valid @RequestBody TagCreateDTO dto) {

    var tag = TagCreateDTO.fromDTO(dto);

    var created = tagService.create(tag);

    return TagDTO.toDTO(created);
  }

  @DeleteMapping(value = "/api/v1/tags/{title}")
  public void delete(@PathVariable("title") String title) {

    tagService.deleteByTitle(title);
  }
}
