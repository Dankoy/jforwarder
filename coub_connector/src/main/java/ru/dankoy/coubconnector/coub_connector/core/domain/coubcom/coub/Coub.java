package ru.dankoy.coubconnector.coub_connector.core.domain.coubcom.coub;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.dankoy.coubconnector.coub_connector.core.domain.Permalink;


@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public final class Coub implements Permalink {

  private long id;

  private String title;

  private String permalink;

  @Setter
  private String url;

  @JsonProperty("published_at")
  private LocalDateTime publishedAt;

}
