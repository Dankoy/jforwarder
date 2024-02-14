package ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.channel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "channels")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Channel {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "title")
  private String title;

  @Column(name = "permalink")
  private String permalink;
}
