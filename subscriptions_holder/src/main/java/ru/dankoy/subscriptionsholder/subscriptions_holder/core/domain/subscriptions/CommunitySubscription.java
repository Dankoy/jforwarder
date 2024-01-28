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
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.community.Community;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.community.Section;

@NamedEntityGraph(name = "community-subscription-full",
    includeAllAttributes = true,
    attributeNodes = {
        @NamedAttributeNode("community"),
        @NamedAttributeNode("section"),
    }
)
@Getter
@ToString(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "community_subs")
public class CommunitySubscription extends Subscription {

  @ManyToOne
  @JoinColumn(name = "community_id")
  private Community community;

  @ManyToOne
  @JoinColumn(name = "section_id")
  private Section section;

}
