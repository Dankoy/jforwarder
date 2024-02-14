package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.communitysub;

import jakarta.validation.constraints.NotNull;
import java.util.HashSet;
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
public class CommunityWithoutSectionsDTO {

  private long id;

  @NotNull private long externalId;

  @NotNull private String name;

  public static CommunityWithoutSectionsDTO toDTO(Community community) {

    return builder()
        .id(community.getId())
        .externalId(community.getExternalId())
        .name(community.getName())
        .build();
  }

  public static Community fromDTO(CommunityWithoutSectionsDTO dto) {

    return new Community(dto.id, dto.externalId, dto.name, new HashSet<>());
  }
}
