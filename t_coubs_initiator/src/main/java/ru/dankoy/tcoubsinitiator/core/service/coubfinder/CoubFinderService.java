package ru.dankoy.tcoubsinitiator.core.service.coubfinder;

import java.util.List;
import ru.dankoy.tcoubsinitiator.core.domain.coubcom.coub.Coub;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.communitysubscription.CommunitySubscription;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.tagsubscription.TagSubscription;

public interface CoubFinderService {

  List<Coub> findUnsentCoubsForCommunitySubscription(CommunitySubscription communitySubscription);


  List<Coub> findUnsentCoubsForTagSubscription(TagSubscription tagSubscription);
}
