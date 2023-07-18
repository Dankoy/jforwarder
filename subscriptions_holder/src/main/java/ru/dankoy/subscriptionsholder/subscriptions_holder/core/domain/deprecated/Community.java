package ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.deprecated;


import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Deprecated(forRemoval = false)
//@Entity
//@Table(name = "communities")
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

  @ManyToOne(targetEntity = Section.class, fetch = FetchType.EAGER)
  @JoinColumn(name = "section_id", referencedColumnName = "id")
  private Section section;

  public Community(String name) {
    this.name = name;
  }

}
