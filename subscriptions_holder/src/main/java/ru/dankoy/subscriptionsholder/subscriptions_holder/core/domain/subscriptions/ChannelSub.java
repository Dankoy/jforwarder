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
import lombok.experimental.SuperBuilder;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.channel.Channel;

@NamedEntityGraph(
    name = "channel-subscription-full-inherited",
    includeAllAttributes = true,
    attributeNodes = {
      @NamedAttributeNode("channel"),
      @NamedAttributeNode("order"),
      @NamedAttributeNode("scope"),
      @NamedAttributeNode("type"),
    })
@Getter
@ToString(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "channel_subs")
public class ChannelSub extends Subscription {

  @ManyToOne
  @JoinColumn(name = "channel_id")
  private Channel channel;

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
