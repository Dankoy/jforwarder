package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.subscription;

import jakarta.validation.constraints.NotEmpty;
import java.util.HashSet;
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
public class SubscriptionCreateCommunityDTO {

  @NotEmpty
  private String name;


  public static SubscriptionCreateCommunityDTO toDTO(Community community) {

    return builder()
        .name(community.getName())
        .build();

  }

  public static Community fromDTO(SubscriptionCreateCommunityDTO dto) {

    return new Community(
        0,
        0,
        dto.name,
        new HashSet<>()
    );

  }

}
