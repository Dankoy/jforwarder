package ru.dankoy.subscriptionsholder.subscriptions_holder.core.specifications.telegramchat.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import ru.dankoy.subscriptionsholder.subscriptions_holder.core.specifications.telegramchat.TelegramChatSearchCriteria;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.specifications.telegramchat.filter.TelegramChatFilter;



@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChatSearchCriteriaToFilter {

    TelegramChatFilter toFilter(TelegramChatSearchCriteria criteria);

}
