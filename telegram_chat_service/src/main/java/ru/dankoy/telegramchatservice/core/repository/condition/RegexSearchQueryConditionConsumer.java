package ru.dankoy.telegramchatservice.core.repository.condition;

import static org.jooq.impl.DSL.*;

import java.util.Objects;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.Table;
import ru.dankoy.telegramchatservice.core.component.jooqfieldparser.JooqFieldParser;
import ru.dankoy.telegramchatservice.core.domain.search.RegexSearchCriteria;

/**
 * Regex creates more problems than fixes search pattern.
 *
 * <p>Switched to DTO
 */
@Slf4j
@Getter
@AllArgsConstructor
public class RegexSearchQueryConditionConsumer<R extends Record>
    implements Consumer<RegexSearchCriteria> {

  private Condition condition;
  private JooqFieldParser parser;
  private Table<R> table;

  // private static final String PLAIN_SQL = "%s%s%s";
  // private static final String LIKE_SQL = "%s like '%%%s%%'";

  // This thing doesn't work. Works only with strings
  @Override
  public void accept(RegexSearchCriteria param) {

    var tableField = param.getKey();
    var value = param.getValue().toString();
    var operation = param.getOperation();

    // checks if field exist in table
    parser.getTableField(table, tableField);

    check(tableField, value, operation);
  }

  private void check(String tableField, Object value, String operation) {

    if (operation.equalsIgnoreCase(">")) {

      // plain sql is bad
      // condition = condition.and(condition(String.format(PLAIN_SQL, tableField,
      // operation, value)));

      condition = condition.and(field(tableField).gt(val(value)));

    } else if (operation.equalsIgnoreCase("<")) {

      condition = condition.and(field(tableField).lt(val(value)));

    } else if (operation.equalsIgnoreCase(":")) {

      var f = table.field(tableField);
      Class<?> clazz = f.getType();

      if (Objects.nonNull(f) && f.getDataType().getType() == String.class) {

        // condition = condition.and(condition(String.format(LIKE_SQL, tableField,
        // value)));

        condition = condition.and(field(tableField).like("%" + value + "%"));

      } else {

        // condition = condition.and(condition(String.format(PLAIN_SQL, tableField, "=",
        // value)));
        condition = condition.and(field(tableField).eq(val(value)));
      }
    }
  }
}
