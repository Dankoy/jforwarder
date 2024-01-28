package ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions;


import jakarta.persistence.Entity;
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
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.tag.Order;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.tag.Scope;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.tag.Tag;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.tag.Type;

@NamedEntityGraph(name = "tag-subscription-full",
    attributeNodes = {
        @NamedAttributeNode("tag"),
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
public class TagSubscription extends Subscription {

  @ManyToOne
  @JoinColumn(name = "tag_id")
  private Tag tag;

  @ManyToOne
  @JoinColumn(name = "order_id")
  private Order order;

  @ManyToOne
  @JoinColumn(name = "scope_id")
  private Scope scope;

  @ManyToOne
  @JoinColumn(name = "type_id")
  private Type type;

}
