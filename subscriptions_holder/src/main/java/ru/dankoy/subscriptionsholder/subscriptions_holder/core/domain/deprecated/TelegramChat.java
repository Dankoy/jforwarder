package ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.deprecated;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Deprecated(forRemoval = false)
// @Entity
// @Table(name = "telegram_chats")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TelegramChat {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "chat_id")
  private long chatId;

  @Column(name = "username")
  private String userName;
}
