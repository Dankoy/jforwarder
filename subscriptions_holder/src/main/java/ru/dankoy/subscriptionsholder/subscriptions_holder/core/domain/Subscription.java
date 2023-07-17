package ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain;


import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "subscription_chat_permalink")
public class Subscription {

  @EmbeddedId
  private CommunityTelegramChatPK communityChat;


  @Column(name = "last_permalink")
  private String lastPermalink;

}
