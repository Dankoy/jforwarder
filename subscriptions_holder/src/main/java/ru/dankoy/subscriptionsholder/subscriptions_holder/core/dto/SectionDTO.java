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
public class SectionDTO {

  private long id;

  @NotNull private String name;

  public static SectionDTO toDTO(Section section) {

    return builder().id(section.getId()).name(section.getName()).build();
  }

  public static Section fromDTO(SectionDTO dto) {

    return new Section(dto.id, dto.name);
  }
}
