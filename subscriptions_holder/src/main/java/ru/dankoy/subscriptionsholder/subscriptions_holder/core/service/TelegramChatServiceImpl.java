package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.Chat;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceNotFoundException;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository.TelegramChatRepository;

@RequiredArgsConstructor
@Service
public class TelegramChatServiceImpl implements TelegramChatService {

  private final TelegramChatRepository telegramChatRepository;

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

  @Transactional(isolation = Isolation.REPEATABLE_READ)
  @Override
  public Chat update(Chat chat) {

    // get the existing chat by id
    var found =
        telegramChatRepository
            .findForUpdateByChatIdAndMessageThreadId(chat.getChatId(), chat.getMessageThreadId())
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        String.format("Chat not found - %d", chat.getChatId())));

    // get created date from the existing chat and set it to the new chat
    chat.setDateCreated(found.getDateCreated());
    chat.setId(found.getId());

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
