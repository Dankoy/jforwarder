package ru.dankoy.coubtagssearcher.core.controller;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dankoy.coubtagssearcher.core.dto.TagDTO;
import ru.dankoy.coubtagssearcher.core.service.tag.TagService;

@RequiredArgsConstructor
@RestController
public class TagController {

  private final TagService tagService;


  @GetMapping(value = "/api/v1/tags", params = {"title"})
  public TagDTO findTagByTitle(@Valid @NotEmpty @NotNull String title) {

    var tag = tagService.findTagByTitle(title);

    return TagDTO.toDTO(tag);
  }

}
