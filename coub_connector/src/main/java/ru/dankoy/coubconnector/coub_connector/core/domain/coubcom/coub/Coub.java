package ru.dankoy.coubconnector.coub_connector.core.domain.coubcom.coub;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.dankoy.coubconnector.coub_connector.core.domain.Permalink;


@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public final class Coub implements Permalink {

  private long id;

  private String title;

  private String permalink;

  @Setter
  private String url;

}
