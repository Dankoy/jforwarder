package ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

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

  @Column(name = "last_permalink")
  private String lastPermalink;

  @BatchSize(size = 10)
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "community_chat",
      joinColumns = @JoinColumn(name = "community_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "telegram_chat_id", referencedColumnName = "id"))
  private List<TelegramChat> chats = new ArrayList<>();

  @ManyToOne(targetEntity = Section.class, fetch = FetchType.EAGER)
  @JoinColumn(name = "section_id", referencedColumnName = "id")
  private Section section;

  public Community(String name) {
    this.name = name;
  }

}
