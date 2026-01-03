package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.Order;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceNotFoundException;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.TestContainerBase;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.subscription.SubscriptionTypeMaker;

@DisplayName("Test OrderServiceImpl ")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({OrderServiceImpl.class})
class OrderServiceImplTest extends TestContainerBase implements SubscriptionTypeMaker, OrderMaker {

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
