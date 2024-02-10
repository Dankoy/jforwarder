package ru.dankoy.telegrambot.config;

public interface BotCredentialsConfig extends FullBotProperties {

  String getName();

  String getToken();
}
