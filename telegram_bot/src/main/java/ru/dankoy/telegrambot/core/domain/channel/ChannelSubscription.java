package ru.dankoy.telegrambot.core.domain.channel;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.dankoy.telegrambot.core.domain.Order;
import ru.dankoy.telegrambot.core.domain.Scope;
import ru.dankoy.telegrambot.core.domain.Type;
import ru.dankoy.telegrambot.core.domain.coub.Coub;
import ru.dankoy.telegrambot.core.domain.subscription.Chat;

@Getter
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChannelSubscription {

  private long id;

  private Channel channel;

  private Chat chat;

  private Order order;

  private Scope scope;

  private Type type;

  private String lastPermalink;

  @Setter private List<Coub> coubs = new ArrayList<>();
}
