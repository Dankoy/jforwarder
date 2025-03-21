package ru.dankoy.telegramchatservice.core.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dankoy.telegramchatservice.core.component.uuidgenerator.UUIDGenerator;
import ru.dankoy.telegramchatservice.core.domain.dto.ChatDTO;
import ru.dankoy.telegramchatservice.core.domain.dto.ChatWithSubs;
import ru.dankoy.telegramchatservice.core.domain.filter.TelegramChatFilter;
import ru.dankoy.telegramchatservice.core.domain.search.RegexSearchCriteria;
import ru.dankoy.telegramchatservice.core.exceptions.ResourceNotFoundException;
import ru.dankoy.telegramchatservice.core.repository.TelegramChatDao;
import ru.dankoy.telegramchatservice.core.service.searchparser.RegexSearchCriteriaParser;

@Service
@RequiredArgsConstructor
public class TelegramChatServiceJooq implements TelegramChatService {

  private final TelegramChatDao dao;
  private final RegexSearchCriteriaParser searchCriteriaParser;
  private final UUIDGenerator uuidgenerator;

  @Override
  public Page<ChatWithSubs> findAllChatsWithSubs(
      List<RegexSearchCriteria> search, Pageable pageable) {
    throw new UnsupportedOperationException("Unimplemented method 'findAllChatsWithSubs'");
  }

  @Override
  public Page<ChatDTO> findAll(String search, Pageable pageable) {

    var criteria = searchCriteriaParser.parse(search);

    return dao.findAll(criteria, pageable);
  }

  @Override
  public Page<ChatDTO> findAll(TelegramChatFilter filter, Pageable pageable) {

    return dao.findAllFiltered(filter, pageable);
  }

  @Override
  @Transactional
  public List<ChatDTO> saveAll(List<ChatDTO> chats) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'saveAll'");
  }

  @Override
  @Transactional
  public ChatDTO save(ChatDTO chat) {

    chat.setId(uuidgenerator.randomUUID());

    return dao.save(chat);
  }

  @Override
  @Transactional
  public ChatDTO update(ChatDTO chat) {
    // get the existing chat by id

    var found =
        dao.findForUpdateById(chat.getId())
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        String.format("Chat not found - %d", chat.getChatId())));

    // get created date from the existing chat and set it to the new chat
    chat.setDateCreated(found.getDateCreated());
    chat.setDateModified(LocalDateTime.now(ZoneOffset.UTC));

    return dao.update(chat);
  }

  @Override
  @Transactional
  public void deleteChats(List<ChatDTO> chats) {
    dao.deleteBatch(chats);
  }

  @Override
  public ChatDTO getByTelegramChatId(long chatId) {
    var optional = dao.findByChatId(chatId);

    return optional.orElseThrow(
        () -> new ResourceNotFoundException("Chat not found by id: " + chatId));
  }

  @Override
  public ChatDTO getByTelegramChatIdAndMessageThreadId(long chatId, Integer messageThreadId) {
    var optional = dao.findByChatIdAndMessageThreadId(chatId, messageThreadId);

    return optional.orElseThrow(
        () -> new ResourceNotFoundException("Chat not found by id: " + chatId));
  }
}
