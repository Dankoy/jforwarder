package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.community.Section;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityCreateSectionDTO {

    @NotNull private String name;

    public static CommunityCreateSectionDTO toDTO(Section section) {

        return builder().name(section.getName()).build();
    }

    public static Section fromDTO(CommunityCreateSectionDTO dto) {

        return new Section(0, dto.name);
    }
}
