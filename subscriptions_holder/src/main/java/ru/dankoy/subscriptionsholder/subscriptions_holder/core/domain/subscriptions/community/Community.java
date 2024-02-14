package ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.community;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

@NamedEntityGraph(
    name = "sections-entity-graph",
    attributeNodes = {@NamedAttributeNode("sections")})
@Entity
@Table(name = "communities")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Community {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "external_id")
  private long externalId;

  @Column(name = "name")
  private String name;

  @BatchSize(size = 10) // not necessary if entity graph is used
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "community_section",
      joinColumns = @JoinColumn(name = "community_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "section_id", referencedColumnName = "id"))
  private Set<Section> sections; // entity graph

  public Community(String name) {
    this.name = name;
  }
}
