package ru.dankoy.tcoubsinitiator.config.coub;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ToString
@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "coub")
public class CoubProperties implements CoubConnectorProperties, CoubRegistryProperties {

  private final Connector connector;
  private final Registry registry;

  @Override
  public String getUrl() {
    return connector.url();
  }

  @Override
  public String getApiUrl() {
    return connector.apiUrl();
  }

  @Override
  public long filterDays() {
    return registry.filter().days();
  }
}
