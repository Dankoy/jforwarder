package ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.deprecated;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Deprecated(forRemoval = false)
// @Entity
// @Table(name = "sections")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Section {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "name")
  private String name;
}
