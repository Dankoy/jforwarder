package ru.dankoy.tcoubsinitiator.core.service.coub;

import ru.dankoy.tcoubsinitiator.core.domain.coubcom.coub.CoubWrapper;

public interface CoubService {

    CoubWrapper getCoubsWrapperForCommunityAndSection(
            String communityName, String sectionName, long page, int perPage);

    CoubWrapper getCoubsWrapperForTag(
            String tag, String orderBy, String type, String scope, long page, int perPage);

    CoubWrapper getCoubsWrapperForChannel(
            String channelPermalink,
            String orderBy,
            String type,
            String scope,
            long page,
            int perPage);
}
