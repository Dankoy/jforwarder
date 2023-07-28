package ru.dankoy.coubtagssearcher.core.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.ToString;
import ru.dankoy.coubtagssearcher.core.domain.Tag;

@ToString
@AllArgsConstructor
public class TagDTO {

  @NotEmpty
  private String title;

  public static TagDTO toDTO(Tag tag) {
    return new TagDTO(tag.getTitle());
  }

  public static Tag fromDTO(TagDTO dto) {
    return new Tag(dto.title);
  }

}
