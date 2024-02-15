package ru.dankoy.subscriptionsholder.subscriptions_holder.core.aspects;

import java.util.List;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.community.Community;

@Aspect
@Component
public class FilterActiveSubscriptionsAspect {

  @Around(
      value =
          "@annotation(ru.dankoy.subscriptionsholder.subscriptions_holder.core.aspects.FilterActiveSubscriptions))")
  public List<Community> filter(ProceedingJoinPoint joinPoint) throws Throwable {

    var logger = getLogger(joinPoint);

    var res = (List<Community>) joinPoint.proceed();

    var filtered =
        res.stream()
            //        .filter(c -> !c.getChats().isEmpty())
            .toList();

    logger.info("Filtered communities - {}", filtered);

    return filtered;
  }

  private Logger getLogger(ProceedingJoinPoint joinPoint) {

    return LoggerFactory.getLogger(joinPoint.getTarget().getClass());
  }
}
