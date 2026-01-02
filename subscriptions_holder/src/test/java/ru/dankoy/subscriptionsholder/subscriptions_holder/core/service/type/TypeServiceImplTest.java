package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.type;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.Type;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceNotFoundException;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.TestContainerBase;

@DisplayName("Test TypeServiceImpl ")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({TypeServiceImpl.class})
class TypeServiceImplTest extends TestContainerBase {

  @PersistenceContext private EntityManager entityManager;

  @Autowired private TypeServiceImpl typeService;

  @DisplayName("getByName expects correct response")
  @Test
  void getByNameTest_expectsCorrectResponse() {

    String TYPE_NAME = "";
    var actual = typeService.getByName(TYPE_NAME);

    var expected = entityManager.find(Type.class, actual.getId());

    assertThat(actual).isEqualTo(expected);
  }

  @DisplayName("getByName expects ResourceNotFoundException")
  @Test
  void getByNameTest_expectsResourceNotFoundException() {

    assertThatThrownBy(() -> typeService.getByName("none"))
        .isInstanceOf(ResourceNotFoundException.class);
  }
}
