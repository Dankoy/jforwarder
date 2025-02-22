package ru.dankoy.telegramchatservice.core.service.searchparser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;
import ru.dankoy.telegramchatservice.core.specifications.telegramchat.criteria.SearchCriteria;

@Service
public class SearchCriteriaParserImpl implements SearchCriteriaParser {

  @Override
  public List<SearchCriteria> parse(String search) {
    List<SearchCriteria> params = new ArrayList<>();
    if (search != null) {
      Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
      Matcher matcher = pattern.matcher(search + ",");
      while (matcher.find()) {
        params.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
      }
    }
    return params;
  }
}
