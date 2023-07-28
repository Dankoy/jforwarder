package ru.dankoy.coubtagssearcher.core.domain.coubcom;


import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.dankoy.coubtagssearcher.core.domain.Tag;

@EqualsAndHashCode
@Data
@ToString
@NoArgsConstructor
public class TagsWrapper {

  private List<Tag> data = new ArrayList<>();
  private Meta meta;

}
