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
import ru.dankoy.kafkamessageproducer.core.domain.subscription.tagsubscription.TagSubscription;
import ru.dankoy.kafkamessageproducer.core.service.converter.MessageConverterProtobuf;
import ru.dankoy.kafkamessageproducer.core.service.messagesender.protobuf.MessageProducerServiceKafkaProtobuf;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TagSubscriptionProtobufController {

  private final MessageConverterProtobuf converter;

  private final MessageProducerServiceKafkaProtobuf tagMessageProducerServiceKafkaProtobuf;

  @PostMapping("/api/v1/tag_subscriptions_protobuf")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void produceMessages(@RequestBody List<TagSubscription> tagSubscriptions) {

    log.info("{}", tagSubscriptions);

    List<ru.dankoy.protobufschemas.protos.domain.subscription.tag.v1.TagSubscription> converted =
        tagSubscriptions.stream().map(converter::convert).flatMap(Collection::stream).toList();

    converted.forEach(tagMessageProducerServiceKafkaProtobuf::send);
  }
}
