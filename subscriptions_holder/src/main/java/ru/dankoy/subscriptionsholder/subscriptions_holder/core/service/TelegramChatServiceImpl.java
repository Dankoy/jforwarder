package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;


import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.TelegramChat;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository.TelegramChatRepository;

@RequiredArgsConstructor
@Service
public class TelegramChatServiceImpl implements TelegramChatService {

  private final TelegramChatRepository telegramChatRepository;


  @Transactional
  @Override
  public List<TelegramChat> saveAll(List<TelegramChat> chats) {

    return telegramChatRepository.saveAll(chats);

  }


  @Transactional
  @Override
  public TelegramChat save(TelegramChat chat) {

    return telegramChatRepository.save(chat);

  }

  @Override
  public void deleteChats(List<TelegramChat> chats) {

    telegramChatRepository.deleteAll(chats);

  }

  @Override
  public Optional<TelegramChat> getByTelegramChatId(String chatId) {

    return telegramChatRepository.findByChatId(chatId);

  }


}
