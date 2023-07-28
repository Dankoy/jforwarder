package ru.dankoy.coubtagssearcher.core.service;

import ru.dankoy.coubtagssearcher.core.domain.Tag;

public interface TagService {

  Tag findTagByTitle(String title);

}
