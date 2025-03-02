package ru.dankoy.telegramchatservice.core.domain.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

/**
 * @deprecated because DDD and microservice separation. For working example see subscription_holder
 *     microservice
 */
@Deprecated(since = "2025-02-25")
@Getter
@Builder
public class ChatWithSubs {

  private long id;

  private long chatId;

  private String type;

  private String title;

  private String firstName;

  private String lastName;

  private String username;

  private boolean active;

  private Integer messageThreadId;

  private LocalDateTime dateCreated;

  private LocalDateTime dateModified;

  private List<SubscriptionWithoutChatDTO> subscriptions = new ArrayList<>();
}
