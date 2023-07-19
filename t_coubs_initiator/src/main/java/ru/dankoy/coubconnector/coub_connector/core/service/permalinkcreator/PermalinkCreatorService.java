package ru.dankoy.coubconnector.coub_connector.core.service.permalinkcreator;

import ru.dankoy.coubconnector.coub_connector.core.domain.coubcom.coub.Permalink;

public interface PermalinkCreatorService {

  void createCommunityPermalink(Permalink permalink);

  void createCoubPermalink(Permalink permalink);
}
