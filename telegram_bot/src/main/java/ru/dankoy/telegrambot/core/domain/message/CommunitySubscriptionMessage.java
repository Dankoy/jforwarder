package ru.dankoy.telegrambot.core.domain.message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.dankoy.telegrambot.core.domain.subscription.community.Community;
import ru.dankoy.telegrambot.core.domain.subscription.community.Section;

@Getter
@SuperBuilder
@ToString(callSuper = true)
@NoArgsConstructor
public final class CommunitySubscriptionMessage extends CoubMessage {
    private Community community;
    private Section section;
}
