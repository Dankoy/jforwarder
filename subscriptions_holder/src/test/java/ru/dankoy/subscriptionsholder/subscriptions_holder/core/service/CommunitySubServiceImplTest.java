package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.Chat;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.CommunitySub;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.community.Section;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceConflictException;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceNotFoundException;

@DisplayName("Test CommunitySubServiceImpl ")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({
  CommunitySubServiceImpl.class,
  CommunityServiceImpl.class,
  SectionServiceImpl.class,
  TelegramChatServiceImpl.class
})
class CommunitySubServiceImplTest extends TestContainerBase
    implements ChatMaker, CommunityMaker, CommunitySubMaker {

  @Autowired private CommunitySubServiceImpl communitySubService;

  @PersistenceContext private EntityManager entityManager;

  private static final Pageable PAGEABLE = PageRequest.of(0, 10);

  @DisplayName("findAll pageable expects correct response")
  @Test
  void findAllTest_expectsCorrectResponse() {

    var community = findCorrectCommunityByNameAndSectionName("memes", "weekly");
    var chat = makeChat(123L);
    entityManager.persist(chat);
    entityManager.flush();

    var subs = makeCorrectCommunitySubs(community, chat);

    subs.forEach(entityManager::persist);
    entityManager.flush();

    Page<CommunitySub> page = communitySubService.findAll(PAGEABLE);

    assertThat(page.getContent()).containsExactlyInAnyOrderElementsOf(subs);
  }

  @DisplayName("findAllByChatActive all active expects correct response")
  @Test
  void findAllByChatActive_activeTrue_expectsCorrectResponse() {

    var community = findCorrectCommunityByNameAndSectionName("memes", "weekly");
    var chat = makeChat(123L);
    entityManager.persist(chat);
    entityManager.flush();

    var subs = makeCorrectCommunitySubs(community, chat);

    subs.forEach(entityManager::persist);
    entityManager.flush();

    Page<CommunitySub> page = communitySubService.findAllByChatActive(true, PAGEABLE);

    assertThat(page.getContent()).containsExactlyInAnyOrderElementsOf(subs);
  }

  @DisplayName("findAllByChatActive all active expects empty list")
  @Test
  void findAllByChatActive_activeFalse_expectsCorrectResponse() {

    var community = findCorrectCommunityByNameAndSectionName("memes", "weekly");
    var chat = makeChat(123L);
    entityManager.persist(chat);
    entityManager.flush();

    var subs = makeCorrectCommunitySubs(community, chat);

    subs.forEach(entityManager::persist);
    entityManager.flush();

    Page<CommunitySub> page = communitySubService.findAllByChatActive(false, PAGEABLE);

    assertThat(page.getContent()).isEmpty();
  }

  @DisplayName("getAllByChatChatId expects correct response")
  @Test
  void getAllByChatChatIdTest_expectsCorrectResponse() {

    var chatId = 123L;

    var community = findCorrectCommunityByNameAndSectionName("memes", "weekly");
    var chat = makeChat(chatId);
    entityManager.persist(chat);
    entityManager.flush();

    var subs = makeCorrectCommunitySubs(community, chat);

    subs.forEach(entityManager::persist);
    entityManager.flush();

    List<CommunitySub> list = communitySubService.getAllByChatChatId(chatId);

    assertThat(list).containsExactlyInAnyOrderElementsOf(subs);
  }

  @DisplayName("getAllByChatChatId with non existing chat expects empty list")
  @Test
  void getAllByChatChatIdTest_nonExistingChat_expectsEmptyList() {

    var chatId = 123L;

    var community = findCorrectCommunityByNameAndSectionName("memes", "weekly");
    var chat = makeChat(chatId);
    entityManager.persist(chat);
    entityManager.flush();

    var subs = makeCorrectCommunitySubs(community, chat);

    subs.forEach(entityManager::persist);
    entityManager.flush();

    List<CommunitySub> list = communitySubService.getAllByChatChatId(1234L);

    assertThat(list).isEmpty();
  }

  @DisplayName("getByCommunityNameSectionNameChatId expects correct response")
  @Test
  void getByCommunityNameSectionNameChatIdTest_expectsCorrectResponse() {

    var chatId = 123L;
    var communityName = "memes";
    var sectionName = "weekly";

    var community = findCorrectCommunityByNameAndSectionName(communityName, sectionName);
    var chat = makeChat(chatId);
    entityManager.persist(chat);
    entityManager.flush();

    var subs = makeCorrectCommunitySubs(community, chat);

    subs.forEach(entityManager::persist);
    entityManager.flush();

    Optional<CommunitySub> actual =
        communitySubService.getByCommunityNameSectionNameChatId(communityName, sectionName, chatId);

    assertThat(actual).hasValue(subs.getFirst());
  }

  @DisplayName("getByCommunityNameSectionNameChatId expects empty optional")
  @Test
  void getByCommunityNameSectionNameChatIdTest_sectionNotExistent_expectsEmptyOptional() {

    var chatId = 123L;
    var communityName = "memes";
    var sectionName = "weekly";

    var community = findCorrectCommunityByNameAndSectionName(communityName, sectionName);
    var chat = makeChat(chatId);
    entityManager.persist(chat);
    entityManager.flush();

    var subs = makeCorrectCommunitySubs(community, chat);

    subs.forEach(entityManager::persist);
    entityManager.flush();

    Optional<CommunitySub> actual =
        communitySubService.getByCommunityNameSectionNameChatId(communityName, "non", chatId);

    assertThat(actual).isEmpty();
  }

  @DisplayName("subscribeChatToCommunity expects correct response")
  @Test
  void subscribeChatToCommunityTest_expectsCorrectResponse() {

    var chatId = 123L;
    var communityName = "memes";
    var sectionName = "weekly";

    var community = findCorrectCommunityByNameAndSectionName(communityName, sectionName);
    var chat = makeChat(chatId);
    entityManager.persist(chat);
    entityManager.flush();

    var sub = makeCorrectCommunitySubs(community, chat).getFirst();

    var actual = communitySubService.subscribeChatToCommunity(sub);

    var expected = entityManager.find(CommunitySub.class, actual.getId());

    assertThat(actual).isEqualTo(expected);
  }

  @DisplayName("subscribeChatToCommunity expects ResourceConflictException")
  @Test
  void subscribeChatToCommunityTest_expectsResourceConflictException() {

    var chatId = 123L;
    var communityName = "memes";
    var sectionName = "weekly";

    var community = findCorrectCommunityByNameAndSectionName(communityName, sectionName);
    var chat = makeChat(chatId);
    entityManager.persist(chat);
    entityManager.flush();

    var subs = makeCorrectCommunitySubs(community, chat);

    subs.forEach(entityManager::persist);
    entityManager.flush();

    var sub = subs.getFirst();

    assertThatThrownBy(() -> communitySubService.subscribeChatToCommunity(sub))
        .isInstanceOf(ResourceConflictException.class);
  }

  @DisplayName(
      "subscribeChatToCommunity section not found by name expects ResourceNotFoundException")
  @Test
  void subscribeChatToCommunityTest_sectionNotFound_expectsResourceNotFoundException() {

    var chatId = 123L;
    var communityName = "memes";
    var sectionName = "non";

    var community = findCorrectCommunityByName(communityName);
    community.setSections(Set.of(new Section(0, sectionName)));
    var chat = makeChat(chatId);
    entityManager.persist(chat);
    entityManager.flush();

    var subs = makeCorrectCommunitySubs(community, chat);
    var sub = subs.getFirst();

    assertThatThrownBy(() -> communitySubService.subscribeChatToCommunity(sub))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @DisplayName("subscribeChatToCommunity additionally create chat expects correct response")
  @Test
  void subscribeChatToCommunityTest_additionallyCreateChat_expectsCorrectResponse() {

    var chatId = 123L;
    var communityName = "memes";
    var sectionName = "weekly";

    var community = findCorrectCommunityByNameAndSectionName(communityName, sectionName);
    var chat = makeChat(chatId);
    entityManager.flush();

    var subs = makeCorrectCommunitySubs(community, chat);
    var sub = subs.getFirst();

    var actual = communitySubService.subscribeChatToCommunity(sub);

    var expected = entityManager.find(CommunitySub.class, actual.getId());
    var expectedChat = entityManager.find(Chat.class, actual.getChat().getId());

    assertThat(expected).isEqualTo(actual);
    assertThat(expectedChat).isNotNull();
  }

  @DisplayName("unsubscribeChatFromCommunity delete existing expects correct response")
  @Test
  void unsubscribeChatFromCommunityTest_deleteExisting_expectsCorrectResponse() {

    var chatId = 123L;
    var communityName = "memes";
    var sectionName = "weekly";

    var community = findCorrectCommunityByNameAndSectionName(communityName, sectionName);
    var chat = makeChat(chatId);
    entityManager.persist(chat);
    entityManager.flush();

    var subs = makeCorrectCommunitySubs(community, chat);

    subs.forEach(entityManager::persist);
    entityManager.flush();

    var sub = subs.getFirst();

    communitySubService.unsubscribeChatFromCommunity(sub);

    var expected = entityManager.find(CommunitySub.class, sub.getId());

    assertThat(expected).isNull();
  }

  @DisplayName("unsubscribeChatFromCommunity delete non existing expects correct response")
  @Test
  void unsubscribeChatFromCommunityTest_deleteNonExisting_expectsCorrectResponse() {

    var chatId = 123L;
    var communityName = "memes";
    var sectionName = "weekly";

    var community = findCorrectCommunityByNameAndSectionName(communityName, sectionName);
    var chat = makeChat(chatId);
    entityManager.persist(chat);
    entityManager.flush();

    var subs = makeCorrectCommunitySubs(community, chat);
    var sub = subs.getFirst();

    communitySubService.unsubscribeChatFromCommunity(sub);

    var expected = entityManager.find(CommunitySub.class, sub.getId());

    assertThat(expected).isNull();
  }
}
