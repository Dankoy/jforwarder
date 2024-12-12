package ru.dankoy.subscriptionsholder.subscriptions_holder.core.jpalisteners;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.Chat;

public class ChatListener {

  @PrePersist
  public void prePersist(Chat chat) {
    if (chat.getDateCreated() == null) {
      chat.setDateCreated(LocalDateTime.now(ZoneOffset.UTC));
    }
  }

  @PreUpdate
  public void preUpdate(Chat chat) {
    if (chat.getDateModified() == null) {
      chat.setDateModified(LocalDateTime.now(ZoneOffset.UTC));
    }
  }
}
