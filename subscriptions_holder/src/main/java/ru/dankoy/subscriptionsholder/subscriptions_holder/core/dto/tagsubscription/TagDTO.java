package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.tagsubscription;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.tag.Tag;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TagDTO {

  private long id;

  private String title;

  public static TagDTO toDTO(Tag tag) {
    return new TagDTO(
        tag.getId(),
        tag.getTitle()
    );
  }

  public static Tag fromDTO(TagDTO dto) {

    return new Tag(
        dto.getId(),
        dto.getTitle()
    );

  }

}
