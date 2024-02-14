package ru.dankoy.coubtagssearcher.core.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dankoy.coubtagssearcher.core.dto.ChannelDTO;
import ru.dankoy.coubtagssearcher.core.service.channel.ChannelService;

@RequiredArgsConstructor
@RestController
public class ChannelController {

    private final ChannelService channelService;

    @GetMapping(
            value = "/api/v1/channels",
            params = {"permalink"})
    public ChannelDTO findChannelByTitle(@Valid @NotEmpty @NotNull String permalink) {

        var chanel = channelService.findChannelByTitle(permalink);

        return ChannelDTO.toDTO(chanel);
    }
}
