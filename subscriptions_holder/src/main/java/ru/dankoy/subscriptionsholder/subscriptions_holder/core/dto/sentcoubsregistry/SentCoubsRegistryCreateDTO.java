package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.sentcoubsregistry;

import jakarta.validation.Valid;
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
public class SentCoubsRegistryCreateDTO {

  @Valid @NotNull private SubscriptionCreateDTO subscription;

  @Valid @NotEmpty private String coubPermalink;

  @Valid @NotNull private LocalDateTime dateTime;

  public static SentCoubsRegistryCreateDTO toDTO(SentCoubsRegistry sentCoubsRegistry) {

    return SentCoubsRegistryCreateDTO.builder()
        .subscription(SubscriptionCreateDTO.toDTO(sentCoubsRegistry.getSubscription()))
        .coubPermalink(sentCoubsRegistry.getCoubPermalink())
        .dateTime(sentCoubsRegistry.getDateTime())
        .build();
  }

  public static SentCoubsRegistry fromDTO(SentCoubsRegistryCreateDTO dto) {

    return new SentCoubsRegistry(
        0,
        SubscriptionCreateDTO.fromDTO(dto.getSubscription()),
        dto.getCoubPermalink(),
        dto.getDateTime());
  }
}
