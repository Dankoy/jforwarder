package ru.dankoy.telegrambot.core.service.bot.commands;

import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import ru.dankoy.telegrambot.core.domain.subscription.community.Community;
import ru.dankoy.telegrambot.core.dto.flow.CreateReplyCommunitiesDto;
import ru.dankoy.telegrambot.core.service.community.CommunityService;

@EqualsAndHashCode(callSuper = true)
@Slf4j
public class CommunitiesCommand extends BotCommand {

  private final transient CommunityService communityService;

  public CommunitiesCommand(String command, String description, CommunityService communityService) {
    super(command, description);
    this.communityService = communityService;
  }

  public CreateReplyCommunitiesDto communities(Message inputMessage) {

    List<Community> communities = communityService.getAll();

    return new CreateReplyCommunitiesDto(inputMessage, communities);
  }
}
