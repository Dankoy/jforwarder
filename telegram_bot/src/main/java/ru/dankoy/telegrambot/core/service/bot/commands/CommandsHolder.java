package ru.dankoy.telegrambot.core.service.bot.commands;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;


@Getter
public class CommandsHolder {

  private final List<BotCommand> commands = new ArrayList<>();

  public void addCommand(BotCommand command) {

    commands.add(command);

  }

}
