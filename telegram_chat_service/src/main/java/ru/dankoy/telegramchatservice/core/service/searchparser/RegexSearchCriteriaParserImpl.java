package ru.dankoy.telegramchatservice.core.service.searchparser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;
import ru.dankoy.telegramchatservice.core.domain.search.RegexSearchCriteria;

@Service
public class RegexSearchCriteriaParserImpl implements RegexSearchCriteriaParser {

  @Override
  public List<RegexSearchCriteria> parse(String search) {
    List<RegexSearchCriteria> params = new ArrayList<>();
    if (search != null) {
      Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
      Matcher matcher = pattern.matcher(search + ",");
      while (matcher.find()) {
        params.add(new RegexSearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
      }
    }
    return params;
  }
}
