package ru.dankoy.subscriptions_scheduler.core.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.dankoy.subscriptions_scheduler.core.domain.subscribtionsholder.Chat;
import ru.dankoy.subscriptions_scheduler.core.dto.subscriptions.ChatWithSubsDTO;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = SubscriptionMapper.class)
public interface ChatWithSubsMapper {

  ChatWithSubsDTO toDto(Chat chat);

  Chat fromDto(ChatWithSubsDTO dto);
}
