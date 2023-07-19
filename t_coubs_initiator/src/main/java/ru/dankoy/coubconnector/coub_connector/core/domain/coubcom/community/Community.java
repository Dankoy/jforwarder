package ru.dankoy.coubconnector.coub_connector.core.domain.coubcom.community;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public final class Community {

  private long id;

  private String title;

  @Setter
  private String url;

}
