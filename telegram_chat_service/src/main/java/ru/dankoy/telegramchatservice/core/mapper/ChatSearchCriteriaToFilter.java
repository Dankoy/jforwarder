package ru.dankoy.telegramchatservice.core.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.dankoy.telegramchatservice.core.domain.filter.TelegramChatFilter;
import ru.dankoy.telegramchatservice.core.domain.search.DtoSearchCriteria;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChatSearchCriteriaToFilter {

  TelegramChatFilter toFilter(DtoSearchCriteria criteria);
}
