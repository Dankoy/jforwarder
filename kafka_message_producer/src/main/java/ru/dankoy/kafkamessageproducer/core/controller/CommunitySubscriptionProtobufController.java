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
import ru.dankoy.kafkamessageproducer.core.domain.subscription.communitysubscription.CommunitySubscription;
import ru.dankoy.kafkamessageproducer.core.service.converter.MessageConverterProtobuf;
import ru.dankoy.kafkamessageproducer.core.service.messagesender.protobuf.MessageProducerServiceKafkaProtobuf;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommunitySubscriptionProtobufController {

  private final MessageConverterProtobuf converter;

  private final MessageProducerServiceKafkaProtobuf communityMessageProducerServiceKafkaProtobuf;

  @PostMapping("/api/v1/subscriptions_protobuf")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void produceMessages(@RequestBody List<CommunitySubscription> communitySubscriptions) {

    log.info("{}", communitySubscriptions);

    List<ru.dankoy.protobufschemas.protos.domain.subscription.community.v1.CommunitySubscription>
        converted =
            communitySubscriptions.stream()
                .map(converter::convert)
                .flatMap(Collection::stream)
                .toList();

    converted.forEach(communityMessageProducerServiceKafkaProtobuf::send);
  }
}
