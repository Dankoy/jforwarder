package ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.deprecated;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

@Deprecated(forRemoval = false)
@AllArgsConstructor
@NoArgsConstructor
@Data
// @Embeddable
public class CommunityTelegramChatPK implements Serializable {

    @BatchSize(size = 3)
    @ManyToOne
    @JoinColumn(name = "community_id")
    private Community community;

    @BatchSize(size = 3)
    @ManyToOne
    @JoinColumn(name = "telegram_chat_id")
    private TelegramChat telegramChat;
}
