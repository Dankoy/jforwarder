package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.community.Community;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.community.Section;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceConflictException;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceNotFoundException;

@DisplayName("Test CommunityServiceImpl ")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({CommunityServiceImpl.class, SectionServiceImpl.class})
class CommunityServiceImplTest extends TestContainerBase implements CommunityMaker {

  @Autowired private CommunityServiceImpl communityService;

  @PersistenceContext private EntityManager entityManager;

  @DisplayName("getAll expected correct")
  @Test
  void getAllExpectedFullList() {

    List<Community> actual = communityService.getAll();

    assertThat(actual).containsExactlyElementsOf(correctCommunities());
  }

  @DisplayName("getAll expected empty list")
  @Test
  void getAllExpectedEmptyList() {

    List<Community> all = entityManager.createQuery("select c from Community c").getResultList();
    all.forEach(entityManager::remove);
    entityManager.flush();

    List<Community> actual = communityService.getAll();

    assertThat(actual).isEmpty();
  }

  @DisplayName("getByName expected correct")
  @Test
  void getByNameTestExpectsCorrectResponse() {

    var name = "memes";

    var expected = findCorrectCommunityByName(name);

    var actual = communityService.getByName(name);

    assertThat(actual).isEqualTo(expected);
  }

  @DisplayName("getByName expected ResourceNotFoundException")
  @Test
  void getByNameTestExpectsResourceNotFoundException() {

    var name = "nonexistent";

    assertThatThrownBy(() -> communityService.getByName(name))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @DisplayName("getByNameAndSectionName expected correct")
  @Test
  void getByNameAndSectionNameTestExpectsCorrectResponse() {

    var name = "memes";
    var sectionName = "weekly";

    var expected = findCorrectCommunityByNameAndSectionName(name, sectionName);

    var actual = communityService.getByNameAndSectionName(name, sectionName);

    assertThat(actual).isEqualTo(expected);
  }

  @DisplayName("getByNameAndSectionName expects ResourceNotFoundException")
  @ParameterizedTest
  @MethodSource("getByNameAndSectionNameExpectsResourceNotFoundException")
  void getByNameAndSectionNameTest_incorrectNames_ExpectsResourceNotFoundException(
      String name, String sectionName) {

    assertThatThrownBy(() -> communityService.getByNameAndSectionName(name, sectionName))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @DisplayName("getByNameAndSectionIn expected correct")
  @Test
  void getByNameAndSectionInTestExpectsCorrectResponse() {

    var name = "memes";
    Set<Section> sections = makeCorrectSections().stream().limit(1).collect(Collectors.toSet());

    var expected = findCorrectCommunityByName(name);

    var actual = communityService.getByNameAndSectionIn(name, sections);

    assertThat(actual).isEqualTo(expected);
  }

  @DisplayName("getByNameAndSectionIn expected ResourceNotFoundException")
  @ParameterizedTest
  @MethodSource("getByNameAndSectionNameInExpectsResourceNotFoundException")
  void getByNameAndSectionInTest_incorrectNames_expectsResourceNotFoundException(
      String communityName, Set<Section> sections) {

    assertThatThrownBy(() -> communityService.getByNameAndSectionIn(communityName, sections))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @DisplayName("create new community with one section expects correct")
  @Test
  void createTestNewCommunityWithOneSectionExpectsCorrectResponse() {

    var toCreate = findCorrectCommunityByNameAndSectionName("memes", "weekly");
    toCreate.setId(0L);
    toCreate.setName("memes2");

    var actual = communityService.create(toCreate);

    var expected = entityManager.find(Community.class, actual.getId());

    assertThat(actual).isEqualTo(expected);
  }

  @DisplayName("create new community with multiple sections expects correct")
  @Test
  void createTestNewCommunityWithMultipleSectionExpectsCorrectResponse() {

    var toCreate = findCorrectCommunityByName("memes");
    toCreate.setId(0L);
    toCreate.setName("memes2");

    var actual = communityService.create(toCreate);
    entityManager.flush();

    var expected = entityManager.find(Community.class, actual.getId());

    assertThat(actual).isEqualTo(expected);
  }

  @DisplayName("create new community with empty sections")
  @Test
  void createTestNewCommunityWithEmptySectionExpectsCorrectResponse() {

    var toCreate = findCorrectCommunityByName("memes");
    toCreate.setId(0L);
    toCreate.setName("memes2");
    toCreate.setSections(new HashSet<>());

    var actual = communityService.create(toCreate);
    entityManager.flush();

    var expected = entityManager.find(Community.class, actual.getId());

    assertThat(actual).isEqualTo(expected);
  }

  @DisplayName("create new community with non existing sections expects ResourceNotFoundException")
  @Test
  void createTestNewCommunityWithNonExistingSectionExpectsResourceNotFoundException() {

    var toCreate = findCorrectCommunityByName("memes");
    toCreate.setId(0L);
    toCreate.setName("memes2");
    toCreate.setSections(Set.of(new Section(0, "non")));

    assertThatThrownBy(() -> communityService.create(toCreate))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @DisplayName("create new community with existing name expects ResourceConflictException")
  @Test
  void createTestNewCommunityWithExistingNameExpectsResourceConflictException() {

    var toCreate = findCorrectCommunityByName("memes");
    toCreate.setId(0L);

    assertThatThrownBy(() -> communityService.create(toCreate))
        .isInstanceOf(ResourceConflictException.class);
  }

  @DisplayName("update non existing community expects ResourceNotFoundException")
  @Test
  void updateTestExpectsResourceNotFoundException() {

    var toUpdate = findCorrectCommunityByName("memes");
    toUpdate.setName("memes2");
    toUpdate.setId(232323L);

    assertThatThrownBy(() -> communityService.update(toUpdate))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @DisplayName("update existing community expects correct response")
  @Test
  void updateTestExpectsCorrectResponse() {

    var toUpdate = findCorrectCommunityByName("memes");
    toUpdate.setName("memes2");

    var actual = communityService.update(toUpdate);
    entityManager.flush();

    var expected = entityManager.find(Community.class, actual.getId());

    assertThat(actual).isEqualTo(expected);
  }

  @DisplayName("delete existing community by community and section names expects correct response")
  @Test
  void deleteTestByCommunityAndSectionNamesExpectsCorrectResponse() {

    var communityName = "memes";
    var sectionName = "weekly";

    var toDelete = findCorrectCommunityByName("memes");

    communityService.delete(communityName, sectionName);
    entityManager.flush();

    var expected = entityManager.find(Community.class, toDelete.getId());

    assertThat(expected).isNull();
  }

  @DisplayName(
      "delete non existing community by community and section names expects correct response")
  @Test
  void deleteNonExistingTestByCommunityAndSectionNamesExpectsCorrectResponse() {

    var communityName = "memes2";
    var sectionName = "weekly";

    communityService.delete(communityName, sectionName);
    entityManager.flush();

    var expectedInt =
        entityManager
            .createQuery("select c from Community c where c.name = :name")
            .setParameter("name", communityName)
            .getFirstResult();

    assertThat(expectedInt).isZero();
  }

  @DisplayName("delete existing community by community name expects correct response")
  @Test
  void deleteTestByCommunityNameExpectsCorrectResponse() {

    var communityName = "memes";

    var toDelete = findCorrectCommunityByName("memes");

    communityService.delete(communityName);
    entityManager.flush();

    var expected = entityManager.find(Community.class, toDelete.getId());

    assertThat(expected).isNull();
  }

  @DisplayName("delete non existing community by community name expects correct response")
  @Test
  void deleteTestNonExistingByCommunityNameExpectsCorrectResponse() {

    var communityName = "memes2";

    communityService.delete(communityName);
    entityManager.flush();

    var expectedInt =
        entityManager
            .createQuery("select c from Community c where c.name = :name")
            .setParameter("name", communityName)
            .getFirstResult();

    assertThat(expectedInt).isZero();
  }

  private static Stream<Arguments> getByNameAndSectionNameExpectsResourceNotFoundException() {
    return Stream.of(
        Arguments.of("memes", "non"),
        Arguments.of("non", "weekly"),
        Arguments.of(" ", "weekly"),
        Arguments.of(null, null),
        Arguments.of("non", "non"));
  }

  private static Stream<Arguments> getByNameAndSectionNameInExpectsResourceNotFoundException() {
    return Stream.of(
        Arguments.of("memes", new HashSet<>()),
        Arguments.of("non", Set.of(new Section(0, "none"))),
        Arguments.of(null, Set.of(new Section(0, "weekly"))),
        Arguments.of("non", Set.of(new Section(0, "weekly"))));
  }
}
