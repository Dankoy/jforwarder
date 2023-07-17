package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.CommunityTelegramChatPK;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityTelegramChatPKDTO {

  @Valid
  @NotNull
  private CommunityDTO community;

  @Valid
  @NotNull
  private TelegramChatDTO telegramChat;

  public static CommunityTelegramChatPKDTO toDTO(CommunityTelegramChatPK communityTelegramChatPK) {

    return builder()
        .community(CommunityDTO.toDTO(communityTelegramChatPK.getCommunity()))
        .telegramChat(TelegramChatDTO.toDTO(communityTelegramChatPK.getTelegramChat()))
        .build();

  }

  public static CommunityTelegramChatPK fromDTO(CommunityTelegramChatPKDTO dto) {

    return new CommunityTelegramChatPK(
        CommunityDTO.fromDTO(dto.getCommunity()),
        TelegramChatDTO.fromDTO(dto.getTelegramChat())
    );

  }

}
