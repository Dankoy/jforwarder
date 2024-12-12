package ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.jpalisteners.ChatListener;

@EntityListeners(ChatListener.class)
@Entity
@Table(name = "chats")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Chat {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "chat_id")
  private long chatId;

  @Column(name = "type")
  private String type;

  @Column(name = "title")
  private String title;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "username")
  private String username;

  @Column(name = "active")
  private boolean active;

  @Column(name = "message_thread_id")
  private Integer messageThreadId;

  @Column(name = "date_created", nullable = false)
  private LocalDateTime dateCreated;

  @Column(name = "date_modified", nullable = true)
  private LocalDateTime dateModified;
}
