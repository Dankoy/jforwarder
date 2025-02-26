package ru.dankoy.telegramchatservice.core.service.jooqfieldparser;

import java.util.Collection;
import org.jooq.Field;
import org.jooq.SortField;
import org.jooq.Table;
import org.springframework.data.domain.Sort;

public interface JooqFieldParser {

  Collection<SortField<?>> getSortFields(Table<?> table, Sort sortSpecification);

  Field<?> getTableField(Table<?> table, String sortFieldName);
}
