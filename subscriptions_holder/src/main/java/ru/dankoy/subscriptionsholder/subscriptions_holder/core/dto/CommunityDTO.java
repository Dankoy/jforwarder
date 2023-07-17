package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
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
public class CommunityDTO {

  private long id;

  @NotNull
  private long externalId;

  @NotNull
  private String name;

  @NotNull
  private SectionDTO section;


  public static CommunityDTO toDTO(Community community) {

    return builder()
        .id(community.getId())
        .externalId(community.getExternalId())
        .name(community.getName())
        .section(SectionDTO.toDTO(community.getSection()))
        .build();

  }

  public static Community fromDTO(CommunityDTO dto) {

    return new Community(
        dto.id,
        dto.externalId,
        dto.name,
        SectionDTO.fromDTO(dto.section)
    );

  }

}
