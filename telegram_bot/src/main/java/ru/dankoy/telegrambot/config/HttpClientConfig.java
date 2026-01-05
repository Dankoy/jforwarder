package ru.dankoy.telegrambot.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import ru.dankoy.telegrambot.core.httpservice.argumentresolver.PageableArgumentResolver;
import ru.dankoy.telegrambot.core.httpservice.coubsmartsearcher.CoubSmartSearcherHttpService;
import ru.dankoy.telegrambot.core.httpservice.subscriptionsholder.SubscriptionsHolderChannelHttpService;
import ru.dankoy.telegrambot.core.httpservice.subscriptionsholder.SubscriptionsHolderChannelSubHttpService;
import ru.dankoy.telegrambot.core.httpservice.subscriptionsholder.SubscriptionsHolderChatHttpService;
import ru.dankoy.telegrambot.core.httpservice.subscriptionsholder.SubscriptionsHolderCommunityHttpService;
import ru.dankoy.telegrambot.core.httpservice.subscriptionsholder.SubscriptionsHolderCommunitySubHttpService;
import ru.dankoy.telegrambot.core.httpservice.subscriptionsholder.SubscriptionsHolderOrderHttpService;
import ru.dankoy.telegrambot.core.httpservice.subscriptionsholder.SubscriptionsHolderTagHttpService;
import ru.dankoy.telegrambot.core.httpservice.subscriptionsholder.SubscriptionsHolderTagSubHttpService;
import ru.dankoy.telegrambot.core.httpservice.telegramchatservice.TelegramChatHttpService;

@Configuration
// @ImportHttpServices(
//     basePackages = "ru.dankoy.tcoubsinitiator.core.httpservice",
//     clientType = HttpServiceGroup.ClientType.REST_CLIENT)
// doesn't work with custom HttpServiceProxyFactory.
public class HttpClientConfig {

  @Primary
  @Bean
  RestClient.Builder restClientBuilder() {
    // this restclient is for eureka and other stuff
    return RestClient.builder();
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
          ClientHttpRequestInterceptor clientLoggerRequestInterceptor) {

    RestClient restClient =
        lbRestClientBuilder.requestInterceptor(clientLoggerRequestInterceptor).build();

    RestClientAdapter adapter = RestClientAdapter.create(restClient);

    return HttpServiceProxyFactory.builder()
        .exchangeAdapter(adapter)
        .customArgumentResolver(new PageableArgumentResolver())
        .build();
  }

  @Bean
  public CoubSmartSearcherHttpService coubHttpService(
      HttpServiceProxyFactory httpServiceProxyFactory) {
    return httpServiceProxyFactory.createClient(CoubSmartSearcherHttpService.class);
  }

  @Bean
  public TelegramChatHttpService messageProducerHttpService(
      HttpServiceProxyFactory httpServiceProxyFactory) {
    return httpServiceProxyFactory.createClient(TelegramChatHttpService.class);
  }

  @Bean
  public SubscriptionsHolderChannelHttpService subscriptionsHolderChannelHttpService(
      HttpServiceProxyFactory httpServiceProxyFactory) {
    return httpServiceProxyFactory.createClient(SubscriptionsHolderChannelHttpService.class);
  }

  @Bean
  public SubscriptionsHolderChannelSubHttpService subscriptionsHolderChannelSubHttpService(
      HttpServiceProxyFactory httpServiceProxyFactory) {
    return httpServiceProxyFactory.createClient(SubscriptionsHolderChannelSubHttpService.class);
  }

  @Bean
  public SubscriptionsHolderChatHttpService subscriptionsHolderChatHttpService(
      HttpServiceProxyFactory httpServiceProxyFactory) {
    return httpServiceProxyFactory.createClient(SubscriptionsHolderChatHttpService.class);
  }

  @Bean
  public SubscriptionsHolderCommunityHttpService subscriptionsHolderCommunityHttpService(
      HttpServiceProxyFactory httpServiceProxyFactory) {
    return httpServiceProxyFactory.createClient(SubscriptionsHolderCommunityHttpService.class);
  }

  @Bean
  public SubscriptionsHolderCommunitySubHttpService subscriptionsHolderCommunitySubHttpService(
      HttpServiceProxyFactory httpServiceProxyFactory) {
    return httpServiceProxyFactory.createClient(SubscriptionsHolderCommunitySubHttpService.class);
  }

  @Bean
  public SubscriptionsHolderOrderHttpService subscriptionsHolderOrderHttpService(
      HttpServiceProxyFactory httpServiceProxyFactory) {
    return httpServiceProxyFactory.createClient(SubscriptionsHolderOrderHttpService.class);
  }

  @Bean
  public SubscriptionsHolderTagHttpService subscriptionsHolderTagHttpService(
      HttpServiceProxyFactory httpServiceProxyFactory) {
    return httpServiceProxyFactory.createClient(SubscriptionsHolderTagHttpService.class);
  }

  @Bean
  public SubscriptionsHolderTagSubHttpService subscriptionsHolderTagSubHttpService(
      HttpServiceProxyFactory httpServiceProxyFactory) {
    return httpServiceProxyFactory.createClient(SubscriptionsHolderTagSubHttpService.class);
  }
}
