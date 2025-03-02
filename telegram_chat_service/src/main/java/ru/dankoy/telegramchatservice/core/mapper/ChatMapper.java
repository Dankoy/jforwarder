package ru.dankoy.telegramchatservice.core.mapper;

import static org.jooq.impl.DSL.uuid;

import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import ru.dankoy.telegramchatservice.core.domain.Chat;
import ru.dankoy.telegramchatservice.core.domain.dto.ChatCreateDTO;
import ru.dankoy.telegramchatservice.core.domain.dto.ChatDTO;
import ru.dankoy.telegramchatservice.core.domain.dto.ChatUpdateDTO;
import ru.dankoy.telegramchatservice.core.domain.jooq.tables.pojos.Chats;
import ru.dankoy.telegramchatservice.core.domain.jooq.tables.records.ChatsRecord;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChatMapper {

  ChatDTO toChatDTO(ChatsRecord chatsRecord);

  @Mapping(target = "id", source = "id", qualifiedByName = "mapUuidToLong")
  Chat toJpaFromDTO(ChatDTO dto);

  @Mapping(target = "id", source = "id", qualifiedByName = "mapLongToUUid")
  ChatDTO fromJpaToDTO(Chat chat);

  @Named("mapUuidToLong")
  default Long mapToBuyerInfoId(UUID uuid) {
    return Math.abs(uuid.getLeastSignificantBits());
  }

  @Named("mapLongToUUid")
  default UUID mapToBuyerInfoId(Long l) {
    return UUID.randomUUID();
  }

  ChatDTO fromChatUpdateDTO(ChatUpdateDTO dto);

  ChatDTO fromChatCreateDTO(ChatCreateDTO dto);

  ChatDTO fromJooqPojo(Chats jooqPojo);

  Chats toJooqPojo(ChatDTO dto);
}
