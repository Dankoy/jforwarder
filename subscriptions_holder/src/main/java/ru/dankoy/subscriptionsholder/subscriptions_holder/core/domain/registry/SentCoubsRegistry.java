package ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.registry;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.Subscription;


@Table(name = "sent_coubs_registry")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SentCoubsRegistry {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  //Нужно хранить по регистру для разных типов подписок, либо вынести общую часть подписок в
  // отдельный класс и хранить в нем id, chat, lastPermalink так как они общие для всех подписок.
  // Таким образом можно связать один архив для любых подписок. А добавление новых подписок
  // будет простым полиморфизмом от родительского класса.

  @ManyToOne
  @JoinColumn(name = "subscription_id")
  private Subscription subscription; // у разных типов подписок разная таблица референса, нобходимо наследование

  @Column(name = "coub_permalink")
  private String coubPermalink;

  @Column(name = "date_time")
  private LocalDateTime dateTime;

}
