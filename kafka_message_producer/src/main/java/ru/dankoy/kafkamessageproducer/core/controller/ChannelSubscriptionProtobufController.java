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
import ru.dankoy.kafkamessageproducer.core.domain.subscription.channelsubscription.ChannelSubscription;
import ru.dankoy.kafkamessageproducer.core.service.converter.MessageConverterProtobuf;
import ru.dankoy.kafkamessageproducer.core.service.messagesender.protobuf.MessageProducerServiceKafkaProtobuf;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChannelSubscriptionProtobufController {

  private final MessageConverterProtobuf converter;

  private final MessageProducerServiceKafkaProtobuf channelMessageProducerServiceKafkaProtobuf;

  @PostMapping("/api/v1/channel_subscriptions_protobuf")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void produceMessages(@RequestBody List<ChannelSubscription> channelSubscriptions) {

    log.info("{}", channelSubscriptions);

    List<ru.dankoy.protobufschemas.protos.domain.subscription.channel.ChannelSubscription>
        converted =
            channelSubscriptions.stream()
                .map(converter::convert)
                .flatMap(Collection::stream)
                .toList();

    converted.forEach(channelMessageProducerServiceKafkaProtobuf::send);
  }
}
