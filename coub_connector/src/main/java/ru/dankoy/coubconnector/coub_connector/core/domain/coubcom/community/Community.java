package ru.dankoy.coubconnector.coub_connector.core.domain.coubcom.community;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import ru.dankoy.coubconnector.coub_connector.core.domain.Permalink;


@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public final class Community implements Permalink {

  private long id;

  private String title;

  private String permalink;

  @Setter
  private String url;

}
