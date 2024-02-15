package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
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
public class CommunityCreateDTO {

  @Min(1)
  private long externalId;

  @NotEmpty private String name;

  @Valid @NotNull private Set<CommunityCreateSectionDTO> sections;

  public static CommunityCreateDTO toDTO(Community community) {

    return builder()
        .externalId(community.getExternalId())
        .name(community.getName())
        .sections(
            community.getSections().stream()
                .map(CommunityCreateSectionDTO::toDTO)
                .collect(Collectors.toSet()))
        .build();
  }

  public static Community fromDTO(CommunityCreateDTO dto) {

    return new Community(
        0,
        dto.externalId,
        dto.name,
        dto.sections.stream().map(CommunityCreateSectionDTO::fromDTO).collect(Collectors.toSet()));
  }
}
