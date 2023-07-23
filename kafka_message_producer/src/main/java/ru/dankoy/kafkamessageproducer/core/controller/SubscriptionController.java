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
import ru.dankoy.kafkamessageproducer.core.domain.message.SubscriptionMessage;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.Subscription;
import ru.dankoy.kafkamessageproducer.core.service.converter.MessageConverter;
import ru.dankoy.kafkamessageproducer.core.service.messagesender.MessageProducerService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SubscriptionController {

  private final MessageConverter converter;

  private final MessageProducerService producerService;

  @PostMapping("/api/v1/subscriptions")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void produceMessages(@RequestBody List<Subscription> subscriptions) {

    log.info("{}", subscriptions);

    for (Subscription subscription: subscriptions) {

      // ascending order
      subscription.getCoubs()
          .sort((emp1, emp2) -> emp1.getPublishedAt().compareTo(emp2.getPublishedAt()));

    }

    List<SubscriptionMessage> converted = subscriptions.stream()
        .map(converter::convert)
        .flatMap(Collection::stream)
        .toList();

    converted.forEach(producerService::send);

  }

}
