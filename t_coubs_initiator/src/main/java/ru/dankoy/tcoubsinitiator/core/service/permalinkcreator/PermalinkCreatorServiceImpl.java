package ru.dankoy.tcoubsinitiator.core.service.permalinkcreator;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.tcoubsinitiator.config.coub.CoubConnectorProperties;
import ru.dankoy.tcoubsinitiator.core.domain.coubcom.coub.Permalink;

@RequiredArgsConstructor
@Service
public class PermalinkCreatorServiceImpl implements PermalinkCreatorService {

  private final CoubConnectorProperties coubProperties;

  @Override
  public void createCommunityPermalink(Permalink permalink) {

    var pl = permalink.getPermalink();
    var coubUrl = coubProperties.getUrl();
    permalink.setUrl(coubUrl + "community/" + pl);

  }

  @Override
  public void createCoubPermalink(Permalink permalink) {

    var pl = permalink.getPermalink();
    var coubUrl = coubProperties.getUrl();
    permalink.setUrl(coubUrl + "view/" + pl);

  }


}
