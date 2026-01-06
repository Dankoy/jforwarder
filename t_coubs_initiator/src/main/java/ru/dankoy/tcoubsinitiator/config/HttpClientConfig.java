package ru.dankoy.tcoubsinitiator.config;

import io.micrometer.observation.ObservationRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import ru.dankoy.tcoubsinitiator.core.httpservice.argumentresolver.PageableArgumentResolver;
import ru.dankoy.tcoubsinitiator.core.httpservice.argumentresolver.TelegramChatFilterArgumentResolver;
import ru.dankoy.tcoubsinitiator.core.httpservice.coub.CoubHttpService;
import ru.dankoy.tcoubsinitiator.core.httpservice.messageproducer.MessageProducerHttpService;
import ru.dankoy.tcoubsinitiator.core.httpservice.registry.SentCoubsRegistryHttpService;
import ru.dankoy.tcoubsinitiator.core.httpservice.subscription.SubscriptionHttpService;
import ru.dankoy.tcoubsinitiator.core.httpservice.telegramchat.TelegramChatHttpService;

@Configuration
// @ImportHttpServices(
//     basePackages = "ru.dankoy.tcoubsinitiator.core.httpservice",
//     clientType = HttpServiceGroup.ClientType.REST_CLIENT)
// doesn't work with custom HttpServiceProxyFactory.
public class HttpClientConfig {

  @Primary
  @Bean
  RestClient.Builder restClientBuilder(ObservationRegistry observationRegistry) {
    // this restclient is for eureka and other stuff
    return RestClient.builder().observationRegistry(observationRegistry);
  }

  @LoadBalanced
  @Bean(name = "lbRestClientBuilder")
  RestClient.Builder lbRestClientBuilder() {
    // this rest client is for http services
    // if use one restclient, then service can't register in eureka
    // because it thinks that localhost is domain name and tries to
    // get ip from load balancer.
    return RestClient.builder();
  }

  @Bean
  HttpServiceProxyFactory httpServiceProxyFactory(
      @Qualifier("lbRestClientBuilder") RestClient.Builder lbRestClientBuilder,
      @Qualifier("clientLoggerRequestInterceptor")
          ClientHttpRequestInterceptor clientLoggerRequestInterceptor,
      Environment env,
      ObservationRegistry observationRegistry) {

    RestClient restClient =
        lbRestClientBuilder
            .requestInterceptor(clientLoggerRequestInterceptor)
            .observationRegistry(observationRegistry)
            .build();

    RestClientAdapter adapter = RestClientAdapter.create(restClient);

    return HttpServiceProxyFactory.builder()
        .exchangeAdapter(adapter)
        .embeddedValueResolver(env::resolvePlaceholders)
        .customArgumentResolver(new PageableArgumentResolver())
        .customArgumentResolver(new TelegramChatFilterArgumentResolver())
        .build();
  }

  @Bean
  public CoubHttpService coubHttpService(HttpServiceProxyFactory httpServiceProxyFactory) {
    return httpServiceProxyFactory.createClient(CoubHttpService.class);
  }

  @Bean
  public MessageProducerHttpService messageProducerHttpService(
      HttpServiceProxyFactory httpServiceProxyFactory) {
    return httpServiceProxyFactory.createClient(MessageProducerHttpService.class);
  }

  @Bean
  public SentCoubsRegistryHttpService sentCoubsRegistryHttpService(
      HttpServiceProxyFactory httpServiceProxyFactory) {
    return httpServiceProxyFactory.createClient(SentCoubsRegistryHttpService.class);
  }

  @Bean
  public SubscriptionHttpService subscriptionHttpService(
      HttpServiceProxyFactory httpServiceProxyFactory) {
    return httpServiceProxyFactory.createClient(SubscriptionHttpService.class);
  }

  @Bean
  public TelegramChatHttpService telegramChatHttpService(
      HttpServiceProxyFactory httpServiceProxyFactory) {
    return httpServiceProxyFactory.createClient(TelegramChatHttpService.class);
  }
}
