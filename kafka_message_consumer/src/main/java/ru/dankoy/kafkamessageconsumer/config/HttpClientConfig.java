package ru.dankoy.kafkamessageconsumer.config;

import io.micrometer.observation.ObservationRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import ru.dankoy.kafkamessageconsumer.core.httpservice.subscriptionsholder.SubscriptionsHolderHttpService;
import ru.dankoy.kafkamessageconsumer.core.httpservice.telegrambot.TelegramBotHttpService;

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
      ObservationRegistry observationRegistry) {

    RestClient restClient =
        lbRestClientBuilder
            .requestInterceptor(clientLoggerRequestInterceptor)
            .observationRegistry(observationRegistry)
            .build();

    RestClientAdapter adapter = RestClientAdapter.create(restClient);

    return HttpServiceProxyFactory.builder().exchangeAdapter(adapter).build();
  }

  @Bean
  public SubscriptionsHolderHttpService subscriptionsHolderHttpService(
      HttpServiceProxyFactory httpServiceProxyFactory) {
    return httpServiceProxyFactory.createClient(SubscriptionsHolderHttpService.class);
  }

  @Bean
  public TelegramBotHttpService telegramBotHttpService(
      HttpServiceProxyFactory httpServiceProxyFactory) {
    return httpServiceProxyFactory.createClient(TelegramBotHttpService.class);
  }
}
