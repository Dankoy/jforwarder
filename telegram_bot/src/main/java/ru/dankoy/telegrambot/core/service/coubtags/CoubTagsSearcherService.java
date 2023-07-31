package ru.dankoy.telegrambot.core.service.coubtags;

import java.util.Optional;
import ru.dankoy.telegrambot.core.domain.tagsubscription.Tag;

public interface CoubTagsSearcherService {

  Optional<Tag> findByTitle(String title);

}
