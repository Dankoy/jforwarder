package ru.dankoy.kafkamessageproducer.core.controller;


import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.dankoy.kafkamessageproducer.core.domain.message.CommunitySubscriptionMessage;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.CommunitySubscription;
import ru.dankoy.kafkamessageproducer.core.service.converter.CommunityMessageConverter;
import ru.dankoy.kafkamessageproducer.core.service.messagesender.CommunityMessageProducerService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommunitySubscriptionController {

  private final CommunityMessageConverter converter;

  private final CommunityMessageProducerService producerService;

  @PostMapping("/api/v1/subscriptions")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void produceMessages(@RequestBody List<CommunitySubscription> communitySubscriptions) {

    log.info("{}", communitySubscriptions);

    List<CommunitySubscriptionMessage> converted = communitySubscriptions.stream()
        .map(converter::convert)
        .flatMap(Collection::stream)
        .toList();

    converted.forEach(producerService::send);

  }

}
