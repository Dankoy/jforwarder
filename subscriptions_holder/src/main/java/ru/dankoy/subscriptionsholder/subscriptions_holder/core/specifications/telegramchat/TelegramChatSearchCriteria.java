package ru.dankoy.subscriptionsholder.subscriptions_holder.core.specifications.telegramchat;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TelegramChatSearchCriteria {

    private Long chatId;

    private String type;

    private String title;

    private String firstName;

    private String lastName;

    private String username;

    private Boolean active;

    private Long messageThreadId;

}
