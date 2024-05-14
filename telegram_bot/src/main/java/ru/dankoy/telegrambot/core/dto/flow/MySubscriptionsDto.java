package ru.dankoy.telegrambot.core.dto.flow;

import java.util.List;
import ru.dankoy.telegrambot.core.domain.subscription.channel.ChannelSubscription;
import ru.dankoy.telegrambot.core.domain.subscription.community.CommunitySubscription;
import ru.dankoy.telegrambot.core.domain.subscription.tag.TagSubscription;

public record MySubscriptionsDto(
    List<CommunitySubscription> communitySubscriptions,
    List<TagSubscription> tagSubscriptions,
    List<ChannelSubscription> channelSubscriptions) {}
