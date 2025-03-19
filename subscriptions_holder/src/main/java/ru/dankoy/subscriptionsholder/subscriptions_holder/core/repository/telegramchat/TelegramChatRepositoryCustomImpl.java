package ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository.telegramchat;

import static java.util.stream.Collectors.groupingBy;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
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
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.specifications.telegramchat.criteria.SearchCriteria;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.specifications.telegramchat.criteria.SearchQueryCriteriaConsumer;

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
                            s.id,
                            s.lastPermalink,
                            UUID.fromString(s.getChatUuid()),
                            s.createdAt,
                            s.modifiedAt);
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

  @Override
  public Page<ChatWithSubs> findAllWithSubsByCriteria(
      List<SearchCriteria> search, Pageable pageable) {

    CriteriaBuilder builder = em.getCriteriaBuilder();
    CriteriaQuery<Chat> query = builder.createQuery(Chat.class);
    Root<Chat> r = query.from(Chat.class);

    Predicate predicate = builder.conjunction();

    SearchQueryCriteriaConsumer<Chat> searchConsumer =
        new SearchQueryCriteriaConsumer<>(predicate, builder, r);

    search.stream().forEach(searchConsumer);

    System.out.println(search);

    predicate = searchConsumer.getPredicate();
    query.where(predicate);
    query.orderBy(QueryUtils.toOrders(pageable.getSort(), r, builder));

    // find paged chats
    TypedQuery<Chat> chatsPagedQuery = em.createQuery(query);

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
select s.id, s.chat_id, s.last_permalink,
s.created_at, s.modified_at, s.chat_uuid
from subscriptions s
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
                            s.id,
                            s.lastPermalink,
                            UUID.fromString(s.getChatUuid()),
                            s.createdAt,
                            s.modifiedAt);
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
    private String chatUuid;

    SubscriptionDTOForNativeQuery(
        long id,
        long chatId,
        String lastPermalink,
        Timestamp createdAt,
        Timestamp modifiedAt,
        String chatUuid) {

      this.id = id;
      this.chatId = chatId;
      this.chatUuid = chatUuid;
      this.lastPermalink = lastPermalink;
      this.createdAt = createdAt != null ? createdAt.toLocalDateTime() : null;
      this.modifiedAt = modifiedAt != null ? modifiedAt.toLocalDateTime() : null;
    }
  }
}
