package ru.dankoy.coubconnector.coub_connector.core.service.permalinkcreator;

import ru.dankoy.coubconnector.coub_connector.core.domain.Permalink;

public interface PermalinkCreatorService {

  void createCommunityPermalink(Permalink permalink);

  void createCoubPermalink(Permalink permalink);
}
