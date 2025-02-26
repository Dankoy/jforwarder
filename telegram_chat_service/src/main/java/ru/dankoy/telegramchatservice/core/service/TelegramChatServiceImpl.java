package ru.dankoy.telegramchatservice.core.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.dankoy.telegramchatservice.core.domain.dto.ChatDTO;
import ru.dankoy.telegramchatservice.core.domain.dto.ChatWithSubs;
import ru.dankoy.telegramchatservice.core.exceptions.ResourceNotFoundException;
import ru.dankoy.telegramchatservice.core.mapper.ChatMapper;
import ru.dankoy.telegramchatservice.core.repository.TelegramChatRepository;
import ru.dankoy.telegramchatservice.core.service.specifications.telegramchat.TelegramChatSpecification;
import ru.dankoy.telegramchatservice.core.service.specifications.telegramchat.criteria.SearchCriteria;
import ru.dankoy.telegramchatservice.core.service.specifications.telegramchat.filter.TelegramChatFilter;

/**
 * @deprecated because DDD and microservice separation. For working example see subscription_holder
 *     microservice
 */
@Deprecated(since = "2025-02-25")
@RequiredArgsConstructor
// @Service
public class TelegramChatServiceImpl implements TelegramChatService {

  private final TelegramChatRepository telegramChatRepository;
  private final ChatMapper chatMapper;

  @Override
  public Page<ChatWithSubs> findAllChatsWithSubs(List<SearchCriteria> search, Pageable pageable) {

    return telegramChatRepository.findAllWithSubsByCriteria(search, pageable);
  }

  @Override
  public Page<ChatDTO> findAll(String search, Pageable pageable) {

    // look in custom repository in subscriptions_holder micrioservice.
    throw new UnsupportedOperationException();
  }

  @Override
  public Page<ChatDTO> findAll(TelegramChatFilter filter, Pageable pageable) {

    var spec = TelegramChatSpecification.filterBy(filter);

    return telegramChatRepository.findAll(spec, pageable).map(chatMapper::fromJpaToDTO);
  }

  @Transactional
  @Override
  public List<ChatDTO> saveAll(List<ChatDTO> chats) {

    var jpas = chats.stream().map(chatMapper::toJpaFromDTO).toList();

    var res = telegramChatRepository.saveAll(jpas);

    return res.stream().map(chatMapper::fromJpaToDTO).toList();
  }

  @Transactional
  @Override
  public ChatDTO save(ChatDTO chat) {

    var jpa = chatMapper.toJpaFromDTO(chat);

    var res = telegramChatRepository.save(jpa);

    return chatMapper.fromJpaToDTO(res);
  }

  @Transactional
  @Override
  public ChatDTO update(ChatDTO chat) {

    var jpa = chatMapper.toJpaFromDTO(chat);

    // get the existing chat by id

    var found =
        telegramChatRepository
            .findForUpdateById(jpa.getId())
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        String.format("Chat not found - %d", chat.getChatId())));

    // get created date from the existing chat and set it to the new chat
    jpa.setDateCreated(found.getDateCreated());

    var res = telegramChatRepository.save(jpa);

    return chatMapper.fromJpaToDTO(res);
  }

  @Transactional
  @Override
  public void deleteChats(List<ChatDTO> chats) {

    var jpas = chats.stream().map(chatMapper::toJpaFromDTO).toList();

    telegramChatRepository.deleteAll(jpas);
  }

  @Override
  public ChatDTO getByTelegramChatId(long chatId) {

    var res = telegramChatRepository.findByChatId(chatId);

    if (res.isPresent()) {
      return chatMapper.fromJpaToDTO(res.get());
    } else {
      throw new ResourceNotFoundException("Chat not found by id: " + chatId);
    }
  }

  @Override
  public ChatDTO getByTelegramChatIdAndMessageThreadId(long chatId, Integer messageThreadId) {

    var res = telegramChatRepository.findByChatIdAndMessageThreadId(chatId, messageThreadId);

    if (res.isPresent()) {
      return chatMapper.fromJpaToDTO(res.get());
    } else {
      throw new ResourceNotFoundException("Chat not found by id: " + chatId);
    }
  }
}
