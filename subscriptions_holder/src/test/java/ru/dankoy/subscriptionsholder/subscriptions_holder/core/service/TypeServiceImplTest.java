package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.Type;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceNotFoundException;

@DisplayName("Test TypeServiceImpl ")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({TypeServiceImpl.class})
class TypeServiceImplTest extends TestContainerBase {

  @PersistenceContext private EntityManager entityManager;

  @Autowired private TypeServiceImpl typeService;

  private final String TYPE_NAME = "";

  @DisplayName("getByName expects correct response")
  @Test
  void getByNameTest_expectsCorrectResponse() {

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
