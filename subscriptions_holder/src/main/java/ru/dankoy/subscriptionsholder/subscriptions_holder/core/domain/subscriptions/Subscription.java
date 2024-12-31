package ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.Chat;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.jpalisteners.SubscriptionListener;

@EntityListeners({SubscriptionListener.class})
@NamedEntityGraph(
    name = "subscription-full",
    attributeNodes = {
      @NamedAttributeNode("id"),
      @NamedAttributeNode("chat"),
    },
    subgraphs = {
      @NamedSubgraph(
          name = "subgraph.community-subscription",
          attributeNodes = {@NamedAttributeNode("community"), @NamedAttributeNode("section")}),
      @NamedSubgraph(
          name = "subgraph.channel-subscription",
          attributeNodes = {
            @NamedAttributeNode("channel"),
            @NamedAttributeNode("order"),
            @NamedAttributeNode("scope"),
            @NamedAttributeNode("type"),
          }),
      @NamedSubgraph(
          name = "subgraph.tag-subscription",
          attributeNodes = {
            @NamedAttributeNode("tag"),
            @NamedAttributeNode("order"),
            @NamedAttributeNode("scope"),
            @NamedAttributeNode("type"),
          })
    })
@Table(name = "subscriptions")
@Data
@SuperBuilder
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Subscription {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private long id;

  @ManyToOne
  @JoinColumn(name = "chat_id")
  private Chat chat;

  @Column(name = "last_permalink")
  private String lastPermalink;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "modified_at", nullable = false)
  private LocalDateTime modifiedAt;
}
