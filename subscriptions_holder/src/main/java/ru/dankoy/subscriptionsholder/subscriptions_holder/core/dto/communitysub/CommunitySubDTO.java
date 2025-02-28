package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.communitysub;

import java.time.LocalDateTime;
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
public class CommunitySubDTO {

  private long id;

  private CommunityWithoutSectionsDTO community;

  private SectionDTO section;

  private ChatDTO chat;

  private UUID chatUuid;

  private String lastPermalink;

  private LocalDateTime createdAt;

  private LocalDateTime modifiedAt;

  public static CommunitySubDTO toDTO(CommunitySub communitySubscription) {

    return CommunitySubDTO.builder()
        .id(communitySubscription.getId())
        .community(CommunityWithoutSectionsDTO.toDTO(communitySubscription.getCommunity()))
        .section(SectionDTO.toDTO(communitySubscription.getSection()))
        .chat(ChatDTO.toDTO(communitySubscription.getChat()))
        .chatUuid(communitySubscription.getChatUuid())
        .lastPermalink(communitySubscription.getLastPermalink())
        .createdAt(communitySubscription.getCreatedAt())
        .modifiedAt(communitySubscription.getModifiedAt())
        .build();
  }

  public static CommunitySub fromDTO(CommunitySubDTO dto) {

    return CommunitySub.builder()
        .id(dto.getId())
        .community(CommunityWithoutSectionsDTO.fromDTO(dto.getCommunity()))
        .section(SectionDTO.fromDTO(dto.getSection()))
        .chat(ChatDTO.fromDTO(dto.getChat()))
        .chatUuid(dto.getChatUuid())
        .lastPermalink(dto.getLastPermalink())
        .createdAt(dto.getCreatedAt())
        .modifiedAt(dto.getModifiedAt())
        .build();
  }
}
