package ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository;

import static java.util.stream.Collectors.groupingBy;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.Chat;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.SubscriptionWithoutChatDTO;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.chat.ChatWithSubs;

@RequiredArgsConstructor
public class TelegramChatRepositoryCustomImpl implements TelegramChatRepositoryCustom {

  @PersistenceContext private final EntityManager em;

  @Override
  public Page<ChatWithSubs> findAllWithSubsBy(Pageable pageable) {

    // custom sorting from pageable
    // String order = StringUtils.collectionToCommaDelimitedString(
    // StreamSupport.stream(sort.spliterator(), false)
    // .map(o -> o.getProperty() + " " + o.getDirection())
    // .collect(Collectors.toList()));

    // """
    // select c from Chat c
    // order by c.id asc
    // """

    var chatsPagedSql = QueryUtils.applySorting("select c from Chat c", pageable.getSort());

    // find paged chats
    TypedQuery<Chat> chatsPagedQuery = em.createQuery(chatsPagedSql, Chat.class);
    TypedQuery<Long> countQuery = em.createQuery("select count(c) from Chat c", Long.class);
    List<Chat> chatListPaged =
        chatsPagedQuery
            .setFirstResult(Integer.parseInt(String.valueOf(pageable.getOffset())))
            .setMaxResults(pageable.getPageSize())
            .getResultList();
    long total = countQuery.getSingleResult();

    var chatsPage = new PageImpl<>(chatListPaged, pageable, total);

    // find all subscriptions for chats in page

    var chatIds = chatListPaged.stream().map(Chat::getId).toList();

    Query subQuery =
        em.createNativeQuery(
                """
                select * from subscriptions s
                where s.chat_id in :chats
                """,
                SubscriptionDTOForNativeQuery.class)
            .setParameter("chats", chatIds);

    @SuppressWarnings("unchecked")
    List<SubscriptionDTOForNativeQuery> subs = subQuery.getResultList();

    // group subscriptions by chat id
    Map<Long, List<SubscriptionDTOForNativeQuery>> subsGroupedByChat =
        subs.stream().collect(groupingBy(SubscriptionDTOForNativeQuery::getChatId));

    return chatsPage.map(
        chat -> {
          var chatSubsOptional = Optional.ofNullable(subsGroupedByChat.get(chat.getId()));

          var chatSubs = chatSubsOptional.orElse(new ArrayList<>());

          var convertedSubs =
              chatSubs.stream()
                  .map(
                      s -> {
                        return new SubscriptionWithoutChatDTO(
                            s.id, s.lastPermalink, s.createdAt, s.modifiedAt);
                      })
                  .toList();

          return ChatWithSubs.builder()
              .id(chat.getId())
              .chatId(chat.getChatId())
              .active(chat.isActive())
              .type(chat.getType())
              .title(chat.getTitle())
              .username(chat.getUsername())
              .lastName(chat.getLastName())
              .firstName(chat.getFirstName())
              .messageThreadId(chat.getMessageThreadId())
              .dateCreated(chat.getDateCreated())
              .dateModified(chat.getDateModified())
              .subscriptions(convertedSubs)
              .build();
        });
  }

  @ToString
  @Getter
  private static class SubscriptionDTOForNativeQuery {
    private long id;
    private long chatId;
    private String lastPermalink;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    SubscriptionDTOForNativeQuery(
        long id, long chatId, String lastPermalink, Timestamp createdAt, Timestamp modifiedAt) {

      this.id = id;
      this.chatId = chatId;
      this.lastPermalink = lastPermalink;
      this.createdAt = createdAt != null ? createdAt.toLocalDateTime() : null;
      this.modifiedAt = modifiedAt != null ? modifiedAt.toLocalDateTime() : null;
    }
  }
}
