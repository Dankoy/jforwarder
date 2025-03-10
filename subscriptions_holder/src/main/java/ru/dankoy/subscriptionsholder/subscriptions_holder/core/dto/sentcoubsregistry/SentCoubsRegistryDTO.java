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
public class SentCoubsRegistryDTO {

  @Valid
  @Min(1)
  @NotNull
  private long id;

  @Valid @NotNull @NotEmpty private String coubPermalink;

  @Valid @NotNull private LocalDateTime dateTime;

  public static SentCoubsRegistryDTO toDTO(SentCoubsRegistry sentCoubsRegistry) {

    return SentCoubsRegistryDTO.builder()
        .id(sentCoubsRegistry.getId())
        .coubPermalink(sentCoubsRegistry.getCoubPermalink())
        .dateTime(sentCoubsRegistry.getDateTime())
        .build();
  }
}
