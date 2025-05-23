/*
 * This file is generated by jOOQ.
 */
package ru.dankoy.telegramchatservice.core.domain.jooq.tables.daos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;
import ru.dankoy.telegramchatservice.core.domain.jooq.tables.Chats;
import ru.dankoy.telegramchatservice.core.domain.jooq.tables.records.ChatsRecord;

/** This class is generated by jOOQ. */
@SuppressWarnings({"all", "unchecked", "rawtypes", "this-escape"})
public class ChatsDao
    extends DAOImpl<
        ChatsRecord, ru.dankoy.telegramchatservice.core.domain.jooq.tables.pojos.Chats, UUID> {

  /** Create a new ChatsDao without any configuration */
  public ChatsDao() {
    super(Chats.CHATS, ru.dankoy.telegramchatservice.core.domain.jooq.tables.pojos.Chats.class);
  }

  /** Create a new ChatsDao with an attached configuration */
  public ChatsDao(Configuration configuration) {
    super(
        Chats.CHATS,
        ru.dankoy.telegramchatservice.core.domain.jooq.tables.pojos.Chats.class,
        configuration);
  }

  @Override
  public UUID getId(ru.dankoy.telegramchatservice.core.domain.jooq.tables.pojos.Chats object) {
    return object.getId();
  }

  /**
   * Fetch records that have <code>id BETWEEN lowerInclusive AND
   * upperInclusive</code>
   */
  public List<ru.dankoy.telegramchatservice.core.domain.jooq.tables.pojos.Chats> fetchRangeOfId(
      UUID lowerInclusive, UUID upperInclusive) {
    return fetchRange(Chats.CHATS.ID, lowerInclusive, upperInclusive);
  }

  /** Fetch records that have <code>id IN (values)</code> */
  public List<ru.dankoy.telegramchatservice.core.domain.jooq.tables.pojos.Chats> fetchById(
      UUID... values) {
    return fetch(Chats.CHATS.ID, values);
  }

  /** Fetch a unique record that has <code>id = value</code> */
  public ru.dankoy.telegramchatservice.core.domain.jooq.tables.pojos.Chats fetchOneById(
      UUID value) {
    return fetchOne(Chats.CHATS.ID, value);
  }

  /** Fetch a unique record that has <code>id = value</code> */
  public Optional<ru.dankoy.telegramchatservice.core.domain.jooq.tables.pojos.Chats>
      fetchOptionalById(UUID value) {
    return fetchOptional(Chats.CHATS.ID, value);
  }

  /**
   * Fetch records that have <code>chat_id BETWEEN lowerInclusive AND
   * upperInclusive</code>
   */
  public List<ru.dankoy.telegramchatservice.core.domain.jooq.tables.pojos.Chats> fetchRangeOfChatId(
      Long lowerInclusive, Long upperInclusive) {
    return fetchRange(Chats.CHATS.CHAT_ID, lowerInclusive, upperInclusive);
  }

  /** Fetch records that have <code>chat_id IN (values)</code> */
  public List<ru.dankoy.telegramchatservice.core.domain.jooq.tables.pojos.Chats> fetchByChatId(
      Long... values) {
    return fetch(Chats.CHATS.CHAT_ID, values);
  }

  /**
   * Fetch records that have <code>type BETWEEN lowerInclusive AND
   * upperInclusive</code>
   */
  public List<ru.dankoy.telegramchatservice.core.domain.jooq.tables.pojos.Chats> fetchRangeOfType(
      String lowerInclusive, String upperInclusive) {
    return fetchRange(Chats.CHATS.TYPE, lowerInclusive, upperInclusive);
  }

  /** Fetch records that have <code>type IN (values)</code> */
  public List<ru.dankoy.telegramchatservice.core.domain.jooq.tables.pojos.Chats> fetchByType(
      String... values) {
    return fetch(Chats.CHATS.TYPE, values);
  }

  /**
   * Fetch records that have <code>title BETWEEN lowerInclusive AND
   * upperInclusive</code>
   */
  public List<ru.dankoy.telegramchatservice.core.domain.jooq.tables.pojos.Chats> fetchRangeOfTitle(
      String lowerInclusive, String upperInclusive) {
    return fetchRange(Chats.CHATS.TITLE, lowerInclusive, upperInclusive);
  }

  /** Fetch records that have <code>title IN (values)</code> */
  public List<ru.dankoy.telegramchatservice.core.domain.jooq.tables.pojos.Chats> fetchByTitle(
      String... values) {
    return fetch(Chats.CHATS.TITLE, values);
  }

  /**
   * Fetch records that have <code>first_name BETWEEN lowerInclusive AND
   * upperInclusive</code>
   */
  public List<ru.dankoy.telegramchatservice.core.domain.jooq.tables.pojos.Chats>
      fetchRangeOfFirstName(String lowerInclusive, String upperInclusive) {
    return fetchRange(Chats.CHATS.FIRST_NAME, lowerInclusive, upperInclusive);
  }

  /** Fetch records that have <code>first_name IN (values)</code> */
  public List<ru.dankoy.telegramchatservice.core.domain.jooq.tables.pojos.Chats> fetchByFirstName(
      String... values) {
    return fetch(Chats.CHATS.FIRST_NAME, values);
  }

  /**
   * Fetch records that have <code>last_name BETWEEN lowerInclusive AND
   * upperInclusive</code>
   */
  public List<ru.dankoy.telegramchatservice.core.domain.jooq.tables.pojos.Chats>
      fetchRangeOfLastName(String lowerInclusive, String upperInclusive) {
    return fetchRange(Chats.CHATS.LAST_NAME, lowerInclusive, upperInclusive);
  }

  /** Fetch records that have <code>last_name IN (values)</code> */
  public List<ru.dankoy.telegramchatservice.core.domain.jooq.tables.pojos.Chats> fetchByLastName(
      String... values) {
    return fetch(Chats.CHATS.LAST_NAME, values);
  }

  /**
   * Fetch records that have <code>username BETWEEN lowerInclusive AND
   * upperInclusive</code>
   */
  public List<ru.dankoy.telegramchatservice.core.domain.jooq.tables.pojos.Chats>
      fetchRangeOfUsername(String lowerInclusive, String upperInclusive) {
    return fetchRange(Chats.CHATS.USERNAME, lowerInclusive, upperInclusive);
  }

  /** Fetch records that have <code>username IN (values)</code> */
  public List<ru.dankoy.telegramchatservice.core.domain.jooq.tables.pojos.Chats> fetchByUsername(
      String... values) {
    return fetch(Chats.CHATS.USERNAME, values);
  }

  /**
   * Fetch records that have <code>active BETWEEN lowerInclusive AND
   * upperInclusive</code>
   */
  public List<ru.dankoy.telegramchatservice.core.domain.jooq.tables.pojos.Chats> fetchRangeOfActive(
      Boolean lowerInclusive, Boolean upperInclusive) {
    return fetchRange(Chats.CHATS.ACTIVE, lowerInclusive, upperInclusive);
  }

  /** Fetch records that have <code>active IN (values)</code> */
  public List<ru.dankoy.telegramchatservice.core.domain.jooq.tables.pojos.Chats> fetchByActive(
      Boolean... values) {
    return fetch(Chats.CHATS.ACTIVE, values);
  }

  /**
   * Fetch records that have <code>message_thread_id BETWEEN lowerInclusive
   * AND upperInclusive</code>
   */
  public List<ru.dankoy.telegramchatservice.core.domain.jooq.tables.pojos.Chats>
      fetchRangeOfMessageThreadId(Integer lowerInclusive, Integer upperInclusive) {
    return fetchRange(Chats.CHATS.MESSAGE_THREAD_ID, lowerInclusive, upperInclusive);
  }

  /** Fetch records that have <code>message_thread_id IN (values)</code> */
  public List<ru.dankoy.telegramchatservice.core.domain.jooq.tables.pojos.Chats>
      fetchByMessageThreadId(Integer... values) {
    return fetch(Chats.CHATS.MESSAGE_THREAD_ID, values);
  }

  /**
   * Fetch records that have <code>date_created BETWEEN lowerInclusive AND
   * upperInclusive</code>
   */
  public List<ru.dankoy.telegramchatservice.core.domain.jooq.tables.pojos.Chats>
      fetchRangeOfDateCreated(LocalDateTime lowerInclusive, LocalDateTime upperInclusive) {
    return fetchRange(Chats.CHATS.DATE_CREATED, lowerInclusive, upperInclusive);
  }

  /** Fetch records that have <code>date_created IN (values)</code> */
  public List<ru.dankoy.telegramchatservice.core.domain.jooq.tables.pojos.Chats> fetchByDateCreated(
      LocalDateTime... values) {
    return fetch(Chats.CHATS.DATE_CREATED, values);
  }

  /**
   * Fetch records that have <code>date_modified BETWEEN lowerInclusive AND
   * upperInclusive</code>
   */
  public List<ru.dankoy.telegramchatservice.core.domain.jooq.tables.pojos.Chats>
      fetchRangeOfDateModified(LocalDateTime lowerInclusive, LocalDateTime upperInclusive) {
    return fetchRange(Chats.CHATS.DATE_MODIFIED, lowerInclusive, upperInclusive);
  }

  /** Fetch records that have <code>date_modified IN (values)</code> */
  public List<ru.dankoy.telegramchatservice.core.domain.jooq.tables.pojos.Chats>
      fetchByDateModified(LocalDateTime... values) {
    return fetch(Chats.CHATS.DATE_MODIFIED, values);
  }
}
