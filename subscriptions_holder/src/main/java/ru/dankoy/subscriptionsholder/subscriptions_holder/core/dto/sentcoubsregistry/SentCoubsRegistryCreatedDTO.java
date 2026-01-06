package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.sentcoubsregistry;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.registry.SentCoubsRegistry;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SentCoubsRegistryCreatedDTO {

  @Valid
  @Min(1)
  @NotNull
  private long id;

  @Valid @NotNull private SubscriptionCreateDTO subscription;

  @Valid @NotEmpty private String coubPermalink;

  @Valid @NotNull private LocalDateTime dateTime;

  public static SentCoubsRegistryCreatedDTO toDTO(SentCoubsRegistry sentCoubsRegistry) {

    return SentCoubsRegistryCreatedDTO.builder()
        .id(sentCoubsRegistry.getId())
        .subscription(SubscriptionCreateDTO.toDTO(sentCoubsRegistry.getSubscription()))
        .coubPermalink(sentCoubsRegistry.getCoubPermalink())
        .dateTime(sentCoubsRegistry.getDateTime())
        .build();
  }

  public static SentCoubsRegistry fromDTO(SentCoubsRegistryCreatedDTO dto) {

    return new SentCoubsRegistry(
        dto.id,
        SubscriptionCreateDTO.fromDTO(dto.getSubscription()),
        dto.getCoubPermalink(),
        dto.getDateTime());
  }
}
