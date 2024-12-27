package ru.dankoy.subscriptions_scheduler.core.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.dankoy.subscriptions_scheduler.core.domain.subscribtionsholder.subscription.Subscription;
import ru.dankoy.subscriptions_scheduler.core.dto.subscriptions.SubscriptionDTO;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = ChatMapper.class)
public interface SubscriptionMapper {

  SubscriptionDTO tDto(Subscription subscription);

  Subscription fromDto(SubscriptionDTO dto);
}
