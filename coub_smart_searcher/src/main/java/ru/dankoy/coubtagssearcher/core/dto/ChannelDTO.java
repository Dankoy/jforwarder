package ru.dankoy.coubtagssearcher.core.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import ru.dankoy.coubtagssearcher.core.domain.Channel;

@Data
@ToString
@AllArgsConstructor
public class ChannelDTO {

  @NotEmpty private String title;

  @NotEmpty private String permalink;

  public static ChannelDTO toDTO(Channel channel) {
    return new ChannelDTO(channel.getTitle(), channel.getPermalink());
  }

  public static Channel fromDTO(ChannelDTO dto) {
    return new Channel(dto.title, dto.permalink);
  }
}
