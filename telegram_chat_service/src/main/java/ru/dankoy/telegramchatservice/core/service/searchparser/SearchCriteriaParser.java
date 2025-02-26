package ru.dankoy.telegramchatservice.core.service.searchparser;

import java.util.List;

import ru.dankoy.telegramchatservice.core.service.specifications.telegramchat.criteria.SearchCriteria;

public interface SearchCriteriaParser {

  List<SearchCriteria> parse(String search);
}
