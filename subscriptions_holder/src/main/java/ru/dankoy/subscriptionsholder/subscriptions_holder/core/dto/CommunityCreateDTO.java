package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.Community;


@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityCreateDTO {

  @Min(1)
  private long externalId;

  @NotEmpty
  private String name;

  @Valid
  @NotNull
  private CommunityCreateSectionDTO section;


  public static CommunityCreateDTO toDTO(Community community) {

    return builder()
        .externalId(community.getExternalId())
        .name(community.getName())
        .section(CommunityCreateSectionDTO.toDTO(community.getSection()))
        .build();

  }

  public static Community fromDTO(CommunityCreateDTO dto) {

    return new Community(
        0,
        dto.externalId,
        dto.name,
        CommunityCreateSectionDTO.fromDTO(dto.section)
    );

  }

}
