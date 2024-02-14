package ru.dankoy.subscriptionsholder.subscriptions_holder.core.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.CommunityCreateDTO;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.CommunityDTO;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.CommunityService;

@RequiredArgsConstructor
@RestController
public class CommunityController {

    private final CommunityService communityService;

    @GetMapping(path = "/api/v1/communities")
    public List<CommunityDTO> getAllCommunities() {

        var c = communityService.getAll();

        return c.stream().map(CommunityDTO::toDTO).toList();
    }

    @GetMapping(path = "/api/v1/communities/{name}")
    public CommunityDTO getCommunitiesByName(@PathVariable(name = "name") String name) {

        var c = communityService.getByName(name);

        return CommunityDTO.toDTO(c);
    }

    @PostMapping(path = "/api/v1/communities")
    public CommunityDTO createCommunity(@Valid @RequestBody CommunityCreateDTO communityDTO) {

        // создает новый community
        // если community уже существует, то выбрасывается ошибка

        var community = CommunityCreateDTO.fromDTO(communityDTO);

        var created = communityService.create(community);

        return CommunityDTO.toDTO(created);
    }

    @DeleteMapping(path = "/api/v1/communities/{communityName}/{sectionName}")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void deleteCommunity(
            @PathVariable(name = "communityName") String communityName,
            @PathVariable(name = "sectionName") String sectionName) {

        communityService.delete(communityName, sectionName);
    }
}
