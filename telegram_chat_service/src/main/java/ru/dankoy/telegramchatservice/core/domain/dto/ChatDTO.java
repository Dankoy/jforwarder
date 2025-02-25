package ru.dankoy.telegramchatservice.core.domain.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatDTO {

    private Long id;
    private Long chatId;
    private String type;
    private String title;
    private String firstName;
    private String lastName;
    private String username;
    private Boolean active;
    private Integer messageThreadId;
    private LocalDateTime dateCreated;
    private LocalDateTime dateModified;

}
