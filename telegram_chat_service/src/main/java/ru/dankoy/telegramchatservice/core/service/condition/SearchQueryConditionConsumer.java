package ru.dankoy.telegramchatservice.core.service.condition;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.dankoy.telegramchatservice.core.service.jooqfieldparser.JooqFieldParser;
import ru.dankoy.telegramchatservice.core.service.specifications.telegramchat.criteria.SearchCriteria;

import static org.jooq.impl.DSL.*;

@Slf4j
@Getter
@AllArgsConstructor
public class SearchQueryConditionConsumer<R extends Record> implements Consumer<SearchCriteria> {

  private Condition condition;
  private JooqFieldParser parser;
  private Table<R> table;

  private static final String PLAIN_SQL = "%s%s%s";
  private static final String LIKE_SQL = "%s like '%%%s%%'";

  @Override
  public void accept(SearchCriteria param) {

    var tableField = param.getKey();
    var value = param.getValue().toString();
    var operation = param.getOperation();

    parser.getTableField(table, tableField);

    check(tableField, value, operation);

  }

  private void check(String tableField, Object value, String operation) {

    if (operation.equalsIgnoreCase(">")) {

      condition = condition.and(condition(String.format(PLAIN_SQL, tableField, operation, value)));

    } else if (operation.equalsIgnoreCase("<")) {

      condition = condition.and(condition(String.format(PLAIN_SQL, tableField, operation, value)));

    } else if (operation.equalsIgnoreCase(":")) {

      var f = table.field(tableField);

      if (Objects.nonNull(f) && f.getDataType().getType() == String.class) {

        condition = condition.and(condition(String.format(LIKE_SQL, tableField, value)));

      } else {

        condition = condition.and(condition(String.format(PLAIN_SQL, tableField, "=", value)));

      }
    }
  }
}
