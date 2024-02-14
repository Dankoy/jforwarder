package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.channelsub;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.channel.Channel;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChannelCreateDTO {

    @NotNull @NotEmpty private String permalink;

    public static ChannelCreateDTO toDTO(Channel channel) {
        return new ChannelCreateDTO(channel.getPermalink());
    }

    public static Channel fromDTO(ChannelCreateDTO dto) {

        return new Channel(0, null, dto.getPermalink());
    }
}
