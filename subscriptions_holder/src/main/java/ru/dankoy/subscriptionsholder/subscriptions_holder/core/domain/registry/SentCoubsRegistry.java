package ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.registry;

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
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.Subscription;

@NamedEntityGraph(
        name = "sent-coubs-registry-full",
        includeAllAttributes = true,
        attributeNodes = {
            @NamedAttributeNode("subscription"),
            @NamedAttributeNode("coubPermalink"),
            @NamedAttributeNode("dateTime")
        })
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

    @ManyToOne
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    @Column(name = "coub_permalink")
    private String coubPermalink;

    @Column(name = "date_time")
    private LocalDateTime dateTime;
}
