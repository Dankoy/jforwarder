package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.Chat;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.chat.ChatWithSubs;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceNotFoundException;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository.TelegramChatRepository;

@RequiredArgsConstructor
@Service
public class TelegramChatServiceImpl implements TelegramChatService {

  private final TelegramChatRepository telegramChatRepository;

  @Override
  public Page<ChatWithSubs> findAllChatsWithSubs(Pageable pageable) {

    return telegramChatRepository.findAllWithSubsBy(pageable);
  }

  @Override
  public Page<Chat> findAll(Pageable pageable) {
    return telegramChatRepository.findAll(pageable);
  }

  @Transactional
  @Override
  public List<Chat> saveAll(List<Chat> chats) {

    return telegramChatRepository.saveAll(chats);
  }

  @Transactional
  @Override
  public Chat save(Chat chat) {

    return telegramChatRepository.save(chat);
  }

  @Transactional
  @Override
  public Chat update(Chat chat) {

    // get the existing chat by id

    var found =
        telegramChatRepository
            .findForUpdateById(chat.getId())
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        String.format("Chat not found - %d", chat.getChatId())));

    // get created date from the existing chat and set it to the new chat
    chat.setDateCreated(found.getDateCreated());

    return telegramChatRepository.save(chat);
  }

  @Transactional
  @Override
  public void deleteChats(List<Chat> chats) {

    telegramChatRepository.deleteAll(chats);
  }

  @Override
  public Optional<Chat> getByTelegramChatId(long chatId) {

    return telegramChatRepository.findByChatId(chatId);
  }

  @Override
  public Optional<Chat> getByTelegramChatIdAndMessageThreadId(
      long chatId, Integer messageThreadId) {

    return telegramChatRepository.findByChatIdAndMessageThreadId(chatId, messageThreadId);
  }
}
