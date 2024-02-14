package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.communitysub;

import jakarta.validation.constraints.NotEmpty;
import java.util.HashSet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.community.Community;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunitySubCreateCommunityDTO {

    @NotEmpty private String name;

    public static CommunitySubCreateCommunityDTO toDTO(Community community) {

        return builder().name(community.getName()).build();
    }

    public static Community fromDTO(CommunitySubCreateCommunityDTO dto) {

        return new Community(0, 0, dto.name, new HashSet<>());
    }
}
