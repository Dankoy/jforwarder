package ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.Table;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.Chat;


@NamedEntityGraph(name = "subscription-full",
    attributeNodes = {
        @NamedAttributeNode("id"),
        @NamedAttributeNode("chat"),
        @NamedAttributeNode("lastPermalink")
    }
)
@Table(name = "subscriptions")
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Subscription {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private long id;

  @ManyToOne
  @JoinColumn(name = "chat_id")
  private Chat chat;


  @Column(name = "last_permalink")
  private String lastPermalink;

}
