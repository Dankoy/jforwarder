package ru.dankoy.telegramchatservice.core.jpalisteners;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import ru.dankoy.telegramchatservice.core.domain.Chat;

/**
 * @deprecated because DDD and microservice separation. For working example see subscription_holder
 *     microservice
 */
@Deprecated(since = "2025-02-25")
public class ChatListener {

  @PrePersist
  public void prePersist(Chat chat) {
    if (chat.getDateCreated() == null) {
      chat.setDateCreated(LocalDateTime.now(ZoneOffset.UTC));
    }
  }

  @PreUpdate
  public void preUpdate(Chat chat) {
    chat.setDateModified(LocalDateTime.now(ZoneOffset.UTC));
  }
}
