package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto;

import jakarta.validation.constraints.NotNull;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.community.Community;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityDTO {

  private long id;

  @NotNull private long externalId;

  @NotNull private String name;

  @NotNull private Set<SectionDTO> sections;

  public static CommunityDTO toDTO(Community community) {

    return builder()
        .id(community.getId())
        .externalId(community.getExternalId())
        .name(community.getName())
        .sections(
            community.getSections().stream().map(SectionDTO::toDTO).collect(Collectors.toSet()))
        .build();
  }

  public static Community fromDTO(CommunityDTO dto) {

    return new Community(
        dto.id,
        dto.externalId,
        dto.name,
        dto.sections.stream().map(SectionDTO::fromDTO).collect(Collectors.toSet()));
  }
}
