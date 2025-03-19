package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.section;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.TestContainerBase;

@DisplayName("Test SectionServiceImpl ")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({SectionServiceImpl.class})
class SectionServiceImplTest extends TestContainerBase implements SectionMaker {

  @Autowired private SectionServiceImpl sectionService;

  @DisplayName("getSectionByName expected correct response")
  @Test
  void getSectionByNameTest_expectsCorrectResponse() {

    var name = "weekly";

    var expected = makeCorrectSections().stream().filter(s -> s.getName().equals(name)).findFirst();

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

    Set<Section> expected = makeCorrectSections().stream().limit(2).collect(Collectors.toSet());

    List<Section> actual = sectionService.getBySectionNames(expected);

    assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
  }

  @DisplayName("getBySectionNames expects empty list")
  @Test
  void getBySectionNamesTest_expectsEmptyList() {

    Set<Section> expected = makeCorrectSections().stream().limit(0).collect(Collectors.toSet());

    List<Section> actual = sectionService.getBySectionNames(expected);

    assertThat(actual).isEmpty();
  }

  private Section findCorrectBySectionName(String name) {

    var optional = makeCorrectSections().stream().filter(s -> s.getName().equals(name)).findFirst();

    return optional.orElseThrow();
  }
}
