package ru.dankoy.telegrambot.core.service.flow;

public enum CommandConstraints {
  COMMAND("command"),
  COMMAND_FIRST_FIELD("first"),
  COMMAND_SECOND_FIELD("second"),
  COMMAND_SUBSCRIPTION_TYPE_FIELD("subscription_type"),
  ;
  private final String constraint;

  CommandConstraints(String constraint) {
    this.constraint = constraint;
  }

  public String getConstraint() {
    return constraint;
  }
}
