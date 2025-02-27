package ru.dankoy.telegramchatservice.core.repository.specifications;

import ru.dankoy.telegramchatservice.core.domain.search.RegexSearchCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchQueryCriteriaConsumer<T> implements Consumer<RegexSearchCriteria> {

  private Predicate predicate;
  private CriteriaBuilder builder;
  private Root<T> r;

  @Override
  public void accept(RegexSearchCriteria param) {
    if (param.getOperation().equalsIgnoreCase(">")) {
      predicate = builder.and(
          predicate,
          builder.greaterThanOrEqualTo(r.get(param.getKey()), param.getValue().toString()));
    } else if (param.getOperation().equalsIgnoreCase("<")) {
      predicate = builder.and(
          predicate,
          builder.lessThanOrEqualTo(r.get(param.getKey()), param.getValue().toString()));
    } else if (param.getOperation().equalsIgnoreCase(":")) {
      if (r.get(param.getKey()).getJavaType() == String.class) {
        predicate = builder.and(
            predicate, builder.like(r.get(param.getKey()), "%" + param.getValue() + "%"));
      } else if (r.get(param.getKey()).getJavaType() == Boolean.class) {
        if (param.getValue().equals("true")) {
          predicate = builder.and(predicate, builder.isTrue(r.get(param.getKey())));
        } else if (param.getValue().equals("false")) {
          predicate = builder.and(predicate, builder.isFalse(r.get(param.getKey())));
        }
      } else {
        predicate = builder.and(predicate, builder.equal(r.get(param.getKey()), param.getValue()));
      }
    }
  }
}
