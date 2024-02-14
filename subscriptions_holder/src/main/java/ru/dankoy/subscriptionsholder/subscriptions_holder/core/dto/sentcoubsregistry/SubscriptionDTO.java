package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.sentcoubsregistry;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.Subscription;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.communitysub.ChatDTO;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionDTO {

    @Valid
    @Min(1)
    private long id;

    @Valid @NotNull private ChatDTO chat;

    public static SubscriptionDTO toDTO(Subscription subscription) {

        return SubscriptionDTO.builder()
                .id(subscription.getId())
                .chat(ChatDTO.toDTO(subscription.getChat()))
                .build();
    }

    public static Subscription fromDTO(SubscriptionDTO dto) {

        return Subscription.builder().id(dto.getId()).chat(ChatDTO.fromDTO(dto.getChat())).build();
    }
}
