package ru.dankoy.telegramchatservice.core.service.searchparser;

import java.util.List;
import ru.dankoy.telegramchatservice.core.domain.search.RegexSearchCriteria;

public interface RegexSearchCriteriaParser {

  List<RegexSearchCriteria> parse(String search);
}
