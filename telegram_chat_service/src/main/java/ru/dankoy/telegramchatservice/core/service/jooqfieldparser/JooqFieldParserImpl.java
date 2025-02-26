package ru.dankoy.telegramchatservice.core.service.jooqfieldparser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import org.jooq.Field;
import org.jooq.SortField;
import org.jooq.Table;
import org.jooq.TableField;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class JooqFieldParserImpl implements JooqFieldParser {

  @Override
  public Collection<SortField<?>> getSortFields(Table<?> table, Sort sortSpecification) {
    Collection<SortField<?>> querySortFields = new ArrayList<>();

    if (sortSpecification == null) {
      return querySortFields;
    }

    Iterator<Sort.Order> specifiedFields = sortSpecification.iterator();

    while (specifiedFields.hasNext()) {
      Sort.Order specifiedField = specifiedFields.next();

      String sortFieldName = specifiedField.getProperty();
      Sort.Direction sortDirection = specifiedField.getDirection();

      Field<?> tableField = getTableField(table, sortFieldName);
      SortField<?> querySortField = convertTableFieldToSortField(tableField, sortDirection);
      querySortFields.add(querySortField);
    }

    return querySortFields;
  }

  @Override
  public Field<?> getTableField(Table<?> table, String sortFieldName) {
    TableField<?, ?> sortField = null;

    Field<?>[] fields = table.fields();
    var list = Arrays.asList(fields);

    var optional =
        list.stream().filter(f -> f.getName().equalsIgnoreCase(sortFieldName)).findFirst();

    var field =
        optional.orElseThrow(
            () ->
                new InvalidDataAccessApiUsageException(
                    String.format("Could not find table field: %s", sortFieldName)));

    sortField = (TableField<?, ?>) field;

    return sortField;
  }

  private SortField<?> convertTableFieldToSortField(
      Field<?> tableField, Sort.Direction sortDirection) {
    if (sortDirection == Sort.Direction.ASC) {
      return tableField.asc();
    } else {
      return tableField.desc();
    }
  }
}
