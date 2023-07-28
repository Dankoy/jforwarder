package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.tagsubscription;


import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.tag.Tag;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TagCreateDTO {

  @NotEmpty
  private String title;

  public static TagCreateDTO toDTO(Tag tag) {
    return new TagCreateDTO(
        tag.getTitle()
    );
  }

  public static Tag fromDTO(TagCreateDTO dto) {

    return new Tag(
        0,
        dto.getTitle()
    );

  }

}
