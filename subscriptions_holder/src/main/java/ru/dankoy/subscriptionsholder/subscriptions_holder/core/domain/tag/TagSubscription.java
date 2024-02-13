package ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.tag;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.Chat;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.TagSub;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.Order;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.Scope;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.tag.Tag;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.Type;

/**
 * @deprecated in favor for inherited classes
 * {@link
 * TagSub}
 */
@Deprecated(since = "2024-01-28")
@NamedEntityGraph(name = "tag-subscription-full",
    attributeNodes = {
        @NamedAttributeNode("tag"),
        @NamedAttributeNode("chat"),
        @NamedAttributeNode("order"),
        @NamedAttributeNode("scope"),
        @NamedAttributeNode("type"),
    }
)
@Getter
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tag_subscriptions")
public class TagSubscription {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private long id;

  @ManyToOne
  @JoinColumn(name = "tag_id")
  private Tag tag;

  @ManyToOne
  @JoinColumn(name = "chat_id")
  private Chat chat;

  @ManyToOne
  @JoinColumn(name = "order_id")
  private Order order;

  @ManyToOne
  @JoinColumn(name = "scope_id")
  private Scope scope;

  @ManyToOne
  @JoinColumn(name = "type_id")
  private Type type;

  @Column(name = "last_permalink")
  private String lastPermalink;

}
