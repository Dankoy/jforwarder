package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.Order;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.SubscriptionType;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceNotFoundException;

@DisplayName("Test OrderServiceImpl ")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({OrderServiceImpl.class})
class OrderServiceImplTest extends TestContainerBase {

  @Autowired private OrderServiceImpl orderService;

  @PersistenceContext private EntityManager entityManager;

  @DisplayName("getByValueAndType expects correct response")
  @Test
  void getByValueAndTypeTest_expectsCorrectResponse() {

    var value = "most_recent";
    var type = "channel";

    var expected = findCorrectByValueAndSubscriptionType(value, type);

    var actual = orderService.getByValueAndType(value, type);

    assertThat(actual).isEqualTo(expected);
  }

  @DisplayName("getByValueAndType expects ResourceNotFoundException")
  @ParameterizedTest
  @MethodSource("getByValueAndTypeExpectsResourceNotFoundExceptionSource")
  void getByValueAndTypeTest_expectsResourceNotFoundException(String value, String type) {

    assertThatThrownBy(() -> orderService.getByValueAndType(value, type))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @DisplayName("getAll expects correct response")
  @Test
  void getAllTest_expectsCorrectResponse() {

    List<Order> orders = orderService.getAll();

    assertThat(orders).containsExactlyInAnyOrderElementsOf(makeCorrectOrders());
  }

  @DisplayName("getAll expects empty list")
  @Test
  void getAllTest_expectsEmptyList() {

    List<Order> all = entityManager.createQuery("select o from Order o").getResultList();
    all.forEach(entityManager::remove);
    entityManager.flush();

    List<Order> orders = orderService.getAll();

    assertThat(orders).isEmpty();
  }

  @DisplayName("getAllBySubscriptionType expects correct response")
  @Test
  void getAllBySubscriptionTypeTest_expectsCorrectResponse() {

    var type = "tag";

    var expected =
        makeCorrectOrders().stream()
            .filter(o -> o.getSubscriptionType().getType().equals(type))
            .toList();

    var actual = orderService.getAllBySubscriptionType(type);

    assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
  }

  @DisplayName("getAllBySubscriptionType expects empty list")
  @Test
  void getAllBySubscriptionTypeTest_expectsEmptyList() {

    var type = "non";

    var actual = orderService.getAllBySubscriptionType(type);

    assertThat(actual).isEmpty();
  }

  private Order findCorrectByValueAndSubscriptionType(String value, String subscriptionType) {

    Optional<Order> expectedOptional =
        makeCorrectOrders().stream()
            .filter(
                c ->
                    c.getValue().equals(value)
                        && c.getSubscriptionType().getType().equals(subscriptionType))
            .findFirst();

    return expectedOptional.orElseThrow();
  }

  private Map<String, SubscriptionType> makeCorrectSubscriptionType() {

    Map<String, SubscriptionType> subs = new HashMap<>();
    subs.put("community", new SubscriptionType(1L, "community"));
    subs.put("tag", new SubscriptionType(2L, "tag"));
    subs.put("channel", new SubscriptionType(3L, "channel"));

    return subs;
  }

  private List<Order> makeCorrectOrders() {

    Map<String, SubscriptionType> subs = makeCorrectSubscriptionType();

    return Stream.of(
            new Order(1L, "likes_count", "top", subs.get("tag")),
            new Order(2L, "newest_popular", "popular", subs.get("tag")),
            new Order(3L, "views_count", "views_count", subs.get("tag")),
            new Order(4L, "newest", "fresh", subs.get("tag")),
            new Order(5L, "likes_count", "most_liked", subs.get("channel")),
            new Order(6L, "newest", "most_recent", subs.get("channel")),
            new Order(7L, "views_count", "most_viewed", subs.get("channel")),
            new Order(8L, "oldest", "oldest", subs.get("channel")))
        .toList();
  }

  private static Stream<Arguments> getByValueAndTypeExpectsResourceNotFoundExceptionSource() {
    return Stream.of(
        Arguments.of("top", "non"),
        Arguments.of("non", "tag"),
        Arguments.of("top", " "),
        Arguments.of(" ", "tag"),
        Arguments.of("top", null),
        Arguments.of(null, "tag"),
        Arguments.of(null, null),
        Arguments.of("non", "non"));
  }
}
