package ru.dankoy.telegramchatservice.core.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import ru.dankoy.telegramchatservice.core.domain.Chat;
import ru.dankoy.telegramchatservice.core.domain.dto.ChatDTO;
import ru.dankoy.telegramchatservice.core.domain.jooq.tables.records.ChatsRecord;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChatMapper {

    ChatDTO toChatDTO(ChatsRecord chatsRecord);

    Chat toJpaFromDTO(ChatDTO dto);

    ChatDTO fromJpaToDTO(Chat chat);

}
