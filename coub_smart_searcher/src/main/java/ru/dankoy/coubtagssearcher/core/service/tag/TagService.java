package ru.dankoy.coubtagssearcher.core.service.tag;

import ru.dankoy.coubtagssearcher.core.domain.Tag;

public interface TagService {

  Tag findTagByTitle(String title);

}
