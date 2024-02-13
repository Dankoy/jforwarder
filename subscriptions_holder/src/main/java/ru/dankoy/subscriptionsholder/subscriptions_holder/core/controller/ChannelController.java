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
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.ChannelCreateDTO;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.channelsub.ChannelDTO;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.ChannelService;

@RequiredArgsConstructor
@RestController
public class ChannelController {

  private final ChannelService channelService;

  @GetMapping(
      value = "/api/v1/channels",
      params = {"title"})
  public ChannelDTO getByTitle(@RequestParam("title") String title) {

    var found = channelService.getByTitle(title);

    return ChannelDTO.toDTO(found);
  }

  @PostMapping(value = "/api/v1/channels")
  public ChannelDTO create(@Valid @RequestBody ChannelCreateDTO dto) {

    var tag = ChannelCreateDTO.fromDTO(dto);

    var created = channelService.create(tag);

    return ChannelDTO.toDTO(created);
  }

  @DeleteMapping(value = "/api/v1/channels/{title}")
  public void delete(@PathVariable("title") String title) {

    channelService.deleteByTitle(title);
  }
}
