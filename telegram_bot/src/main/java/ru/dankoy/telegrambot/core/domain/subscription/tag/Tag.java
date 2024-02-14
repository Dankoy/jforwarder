package ru.dankoy.telegrambot.core.domain.subscription.tag;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Tag {

    private long id;

    private String title;

    public Tag(String title) {
        this.title = title;
    }
}
