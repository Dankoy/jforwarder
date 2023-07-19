package ru.dankoy.tcoubsinitiator.core.domain.coubcom.coub;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class CoubWrapper {

  private int page;

  @JsonProperty("per_page")
  private int perPage;

  private int next;

  @Getter
  private List<Coub> coubs;

}
