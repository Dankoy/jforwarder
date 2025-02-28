package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.communitysub;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.CommunitySub;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.SectionDTO;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunitySubCreateDTO {

  @Valid @NotNull private CommunitySubCreateCommunityDTO community;

  @Valid @NotNull private SectionDTO section;

  @Valid @NotNull private ChatCreateDTO chat;

  @Valid @NotNull private UUID chatUuid;

  private String lastPermalink;

  public static CommunitySubCreateDTO toDTO(CommunitySub communitySubscription) {

    return CommunitySubCreateDTO.builder()
        .community(CommunitySubCreateCommunityDTO.toDTO(communitySubscription.getCommunity()))
        .section(SectionDTO.toDTO(communitySubscription.getSection()))
        .chat(ChatCreateDTO.toDTO(communitySubscription.getChat()))
        .chatUuid(communitySubscription.getChatUuid())
        .lastPermalink(communitySubscription.getLastPermalink())
        .build();
  }

  public static CommunitySub fromDTO(CommunitySubCreateDTO dto) {

    return CommunitySub.builder()
        .id(0)
        .community(CommunitySubCreateCommunityDTO.fromDTO(dto.getCommunity()))
        .section(SectionDTO.fromDTO(dto.getSection()))
        .chat(ChatCreateDTO.fromDTO(dto.getChat()))
        .chatUuid(dto.getChatUuid())
        .lastPermalink(dto.getLastPermalink())
        .build();
  }
}
