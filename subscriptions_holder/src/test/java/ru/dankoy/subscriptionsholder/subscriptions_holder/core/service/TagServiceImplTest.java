package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.tag.Tag;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceConflictException;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceNotFoundException;

@DisplayName("Test TagServiceImpl ")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({TagServiceImpl.class})
class TagServiceImplTest extends TestContainerBase implements TagMaker {

  @PersistenceContext private EntityManager entityManager;

  @Autowired private TagServiceImpl tagService;

  private static final String TITLE = "title";

  @DisplayName("getByTitle expects correct response")
  @Test
  void getByTitleTest_expectsCorrectResponse() {

    var expected = makeCorrectTag(TITLE);
    entityManager.persist(expected);
    entityManager.flush();

    var actual = tagService.getByTitle(TITLE);

    assertThat(actual).isEqualTo(expected);
  }

  @DisplayName("getByTitle expects ResourceNotFoundException")
  @Test
  void getByTitleTest_expectsResourceNotFoundException() {

    assertThatThrownBy(() -> tagService.getByTitle(TITLE))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @DisplayName("create expects correct response")
  @Test
  void createTest_expectsCorrectResponse() {

    var toCreate = makeCorrectTag(TITLE);

    var actual = tagService.create(toCreate);

    var expected = entityManager.find(Tag.class, actual.getId());

    assertThat(actual).isEqualTo(expected);
  }

  @DisplayName("create expects ResourceConflictException")
  @Test
  void createTest_expectsResourceConflictException() {

    var toCreate = makeCorrectTag(TITLE);
    entityManager.persist(toCreate);
    entityManager.flush();

    assertThatThrownBy(() -> tagService.create(toCreate))
        .isInstanceOf(ResourceConflictException.class);
  }

  @DisplayName("modify expects correct response")
  @Test
  void modifyTest_expectCorrectResponse() {

    var toCreate = makeCorrectTag(TITLE);
    entityManager.persist(toCreate);
    entityManager.flush();

    var toModify = new Tag(toCreate.getId(), "new");

    var actual = tagService.modify(toModify);

    var expected = entityManager.find(Tag.class, toModify.getId());

    assertThat(actual).isEqualTo(expected);
  }

  @DisplayName("modify expects ResourceNotFoundException")
  @Test
  void modifyTest_expectsResourceNotFoundException() {

    var toModify = new Tag(1L, "new");

    assertThatThrownBy(() -> tagService.modify(toModify))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @DisplayName("delete expects correct response")
  @Test
  void deleteByTitleTest_expectsCorrectResponse() {

    var toCreate = makeCorrectTag(TITLE);
    entityManager.persist(toCreate);
    entityManager.flush();

    tagService.deleteByTitle(TITLE);

    var actual = entityManager.find(Tag.class, toCreate.getId());

    assertThat(actual).isNull();
  }
}
