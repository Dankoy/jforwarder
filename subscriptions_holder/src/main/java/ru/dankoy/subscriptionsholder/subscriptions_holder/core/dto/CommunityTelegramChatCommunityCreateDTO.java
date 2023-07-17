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
public class CommunityTelegramChatCommunityCreateDTO {

  @NotEmpty
  private String name;

  @Valid
  @NotNull
  private CommunityCreateSectionDTO section;


  public static CommunityTelegramChatCommunityCreateDTO toDTO(Community community) {

    return builder()
        .name(community.getName())
        .section(CommunityCreateSectionDTO.toDTO(community.getSection()))
        .build();

  }

  public static Community fromDTO(CommunityTelegramChatCommunityCreateDTO dto) {

    return new Community(
        0,
        0,
        dto.name,
        CommunityCreateSectionDTO.fromDTO(dto.section)
    );

  }

}
