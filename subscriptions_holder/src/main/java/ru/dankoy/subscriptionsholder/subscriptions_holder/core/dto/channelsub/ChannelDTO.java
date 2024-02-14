package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.channelsub;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.channel.Channel;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChannelDTO {

  private long id;

  private String title;
  private String permalink;

  public static ChannelDTO toDTO(Channel channel) {
    return new ChannelDTO(channel.getId(), channel.getTitle(), channel.getPermalink());
  }

  public static Channel fromDTO(ChannelDTO dto) {

    return new Channel(dto.getId(), dto.getTitle(), dto.getPermalink());
  }
}
