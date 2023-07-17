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
public class CommunityTelegramChatPKCreateDTO {

  @Valid
  @NotNull
  private CommunityTelegramChatCommunityCreateDTO community;

  @Valid
  @NotNull
  private TelegramChatCreateDTO telegramChat;

  public static CommunityTelegramChatPKCreateDTO toDTO(
      CommunityTelegramChatPK communityTelegramChatPK) {

    return builder()
        .community(
            CommunityTelegramChatCommunityCreateDTO.toDTO(communityTelegramChatPK.getCommunity()))
        .telegramChat(TelegramChatCreateDTO.toDTO(communityTelegramChatPK.getTelegramChat()))
        .build();

  }

  public static CommunityTelegramChatPK fromDTO(CommunityTelegramChatPKCreateDTO dto) {

    return new CommunityTelegramChatPK(
        CommunityTelegramChatCommunityCreateDTO.fromDTO(dto.getCommunity()),
        TelegramChatCreateDTO.fromDTO(dto.getTelegramChat())
    );

  }

}
