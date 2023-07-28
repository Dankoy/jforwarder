package ru.dankoy.subscriptionsholder.subscriptions_holder.core.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.Tag;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.TagService;

@RequiredArgsConstructor
@RestController
public class TagController {

  private final TagService tagService;

  @GetMapping(value = "/api/v1/tags", params = {"title"})
  public Tag getByTitle(@RequestParam("title") String title) {

    return tagService.getByTitle(title);

  }

  @PostMapping(value = "/api/v1/tags")
  public Tag create(@RequestBody Tag tag) {

    return tagService.create(tag);

  }

  @DeleteMapping(value = "/api/v1/tags/{title}")
  public void delete(@PathVariable("title") String title) {

    tagService.deleteByTitle(title);

  }

}
