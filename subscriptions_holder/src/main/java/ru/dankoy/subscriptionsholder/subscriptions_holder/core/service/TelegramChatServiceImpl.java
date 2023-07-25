package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;


import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.Chat;
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

  @Transactional
  @Override
  public void deleteChats(List<Chat> chats) {

    telegramChatRepository.deleteAll(chats);

  }

  @Override
  public Optional<Chat> getByTelegramChatId(long chatId) {

    return telegramChatRepository.findByChatId(chatId);

  }


}
