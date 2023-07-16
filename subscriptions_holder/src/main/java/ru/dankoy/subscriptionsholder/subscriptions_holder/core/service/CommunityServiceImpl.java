package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;


import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.Community;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.TelegramChat;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceNotFoundException;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.SubscriptionHolderException;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository.CommunityRepository;

@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService {

  private final CommunityRepository communityRepository;

  private final TelegramChatService telegramChatService;

  @Override
  public List<Community> getAll() {
    return communityRepository.findAll();
  }

  @Override
  public Community getByName(String name) {
    var optional = communityRepository.getByName(name);

    return optional.orElseThrow(
        () -> new ResourceNotFoundException(String.format("Not found - %s", name)));

  }

  @Override
  @Transactional
  public Community create(Community community) {

    var existingOptional = communityRepository.getByName(community.getName());

    if (existingOptional.isPresent()) {
      throw new SubscriptionHolderException(
          String.format("Already exists - %s", community.getName()));
    } else {
      community.setChats(new ArrayList<>());
      return communityRepository.save(community);
    }

  }

  @Override
  @Transactional
  public Community update(Community community) {
    return communityRepository.save(community);
  }

  @Override
  @Transactional
  public Community addChat(String communityName, TelegramChat chat) {

    var existingOptional = communityRepository.getByName(communityName);
    var optionalChat = telegramChatService.getByTelegramChatId(chat.getChatId());

    if (existingOptional.isPresent()) {

      var community = existingOptional.get();

      optionalChat.ifPresentOrElse(
          c -> community.getChats().add(c),
          () -> {
            var savedChat = telegramChatService.save(chat);
            community.getChats().add(savedChat);
          });

      return communityRepository.save(community);

    } else {

      throw new ResourceNotFoundException(
          String.format("Not found - %s", communityName));

    }


  }

  @Override
  @Transactional
  public void delete(String name) {

    var existingOptional = communityRepository.getByName(name);

    existingOptional.ifPresent(communityRepository::delete);

  }


  @Override
  @Transactional
  public void deleteChatFromCommunity(String communityName, TelegramChat chat) {

    var existingOptional = communityRepository.getByName(communityName);

    existingOptional.ifPresent(c -> {
      c.getChats().remove(chat);
      communityRepository.save(c);
    });

  }


}
