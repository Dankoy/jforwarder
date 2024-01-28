package ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain;


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
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.CommunitySubs;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.community.Community;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.community.Section;

/**
 * @deprecated in favor for inherited classes
 * {@link
 * CommunitySubs}
 */
@Deprecated(since = "2024-01-28")
@NamedEntityGraph(name = "community-subscription-full",
    attributeNodes = {@NamedAttributeNode("community"),
        @NamedAttributeNode("section"),
        @NamedAttributeNode("chat")}
)
@Getter
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "community_subscriptions")
public class CommunitySubscription {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private long id;

  @ManyToOne
  @JoinColumn(name = "community_id")
  private Community community;

  @ManyToOne
  @JoinColumn(name = "section_id")
  private Section section;

  @ManyToOne
  @JoinColumn(name = "chat_id")
  private Chat chat;


  @Column(name = "last_permalink")
  private String lastPermalink;

}
