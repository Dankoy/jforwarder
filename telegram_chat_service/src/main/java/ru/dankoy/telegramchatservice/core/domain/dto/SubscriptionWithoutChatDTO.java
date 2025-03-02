package ru.dankoy.telegramchatservice.core.domain.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @deprecated because DDD and microservice separation. For working example see subscription_holder
 *     microservice
 */
@Deprecated(since = "2025-02-25")
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionWithoutChatDTO {

  @Min(1)
  private long id;

  @NotEmpty private String lastPermalink;

  @NotNull private LocalDateTime createdAt;

  @NotNull private LocalDateTime modifiedAt;
}
