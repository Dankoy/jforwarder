package ru.dankoy.coubconnector.coub_connector.config;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ToString
@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "coub.connector")
//@ConstructorBinding
public class CoubProperties {

  private final String url;
  private final String apiUrl;

}
