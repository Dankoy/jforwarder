package ru.dankoy.telegrambot.core.exceptions;

public class BotException extends RuntimeException {

    public BotException(String message) {
        super(message);
    }

    public BotException(String message, Exception e) {
        super(message, e);
    }
}
