package ru.dankoy.telegramchatservice.core.specifications.telegramchat.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.dankoy.telegramchatservice.core.specifications.telegramchat.TelegramChatSearchCriteria;
import ru.dankoy.telegramchatservice.core.specifications.telegramchat.filter.TelegramChatFilter;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChatSearchCriteriaToFilter {

  TelegramChatFilter toFilter(TelegramChatSearchCriteria criteria);
}
