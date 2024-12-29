package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.chat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.SubscriptionWithoutChatDTO;

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
