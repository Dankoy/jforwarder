package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.community.Section;

@DisplayName("Test SectionServiceImpl ")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({SectionServiceImpl.class})
class SectionServiceImplTest extends TestContainerBase {

  @Autowired private SectionServiceImpl sectionService;

  @DisplayName("getSectionByName expected correct response")
  @Test
  void getSectionByNameTest_expectsCorrectResponse() {

    var name = "weekly";

    var expected = makeCorrectList().stream().filter(s -> s.getName().equals(name)).findFirst();

    var actual = sectionService.getSectionByName(name);

    assertThat(actual).isEqualTo(expected);
  }

  @DisplayName("getSectionByName expected empty optional")
  @ParameterizedTest
  @ValueSource(strings = {"non"})
  @EmptySource
  @NullSource
  void getSectionByNameTest_expectsEmptyOptional(String name) {

    var actual = sectionService.getSectionByName(name);

    assertThat(actual).isEmpty();
  }

  @DisplayName("getBySectionNames expects correct response")
  @Test
  void getBySectionNamesTest_expectsCorrectResponse() {

    Set<Section> expected = makeCorrectList().stream().limit(2).collect(Collectors.toSet());

    List<Section> actual = sectionService.getBySectionNames(expected);

    assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
  }

  @DisplayName("getBySectionNames expects empty list")
  @Test
  void getBySectionNamesTest_expectsEmptyList() {

    Set<Section> expected = makeCorrectList().stream().limit(0).collect(Collectors.toSet());

    List<Section> actual = sectionService.getBySectionNames(expected);

    assertThat(actual).isEmpty();
  }

  private Section findCorrectBySectionName(String name) {

    var optional = makeCorrectList().stream().filter(s -> s.getName().equals(name)).findFirst();

    return optional.orElseThrow();
  }

  private Set<Section> makeCorrectList() {

    return Stream.of(
            new Section(1L, "daily"),
            new Section(2L, "weekly"),
            new Section(3L, "monthly"),
            new Section(4L, "quarter"),
            new Section(5L, "half"),
            new Section(6L, "rising"),
            new Section(7L, "fresh"))
        .collect(Collectors.toSet());
  }
}
