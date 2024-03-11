package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.Scope;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceNotFoundException;

@DisplayName("Test ScopeServiceImpl ")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({ScopeServiceImpl.class})
class ScopeServiceImplTest extends TestContainerBase {

  @Autowired private ScopeServiceImpl scopeService;

  @DisplayName("getByName expects correct response")
  @Test
  void getByNameTest_expectsCorrectResponse() {

    var name = "all";

    var expected = new Scope(1L, "all");

    var actual = scopeService.getByName(name);

    assertThat(actual).isEqualTo(expected);
  }

  @DisplayName("getByName expects ResourceNotFoundException")
  @ParameterizedTest
  @ValueSource(strings = {"non"})
  @EmptySource
  @NullSource
  void getByNameTest_expectsResourceNotFoundException(String name) {

    assertThatThrownBy(() -> scopeService.getByName(name))
        .isInstanceOf(ResourceNotFoundException.class);
  }
}
