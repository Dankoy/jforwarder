package ru.dankoy.telegramchatservice.core.component.converter;

import java.util.Objects;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jooq.Converter;
import ru.dankoy.telegramchatservice.core.exceptions.JooqConverterException;

public class UUIDConverterJooq implements Converter<String, UUID> {

  @Override
  public UUID from(String databaseObject) {

    checkNull(databaseObject);

    return UUID.fromString(databaseObject);
  }

  @Override
  public String to(UUID userObject) {

    checkNull(userObject);

    return userObject.toString();
  }

  @Override
  public @NotNull Class<String> fromType() {
    return String.class;
  }

  @Override
  public @NotNull Class<UUID> toType() {
    return UUID.class;
  }

  private void checkNull(Object o) {
    if (Objects.isNull(o)) {
      throw new JooqConverterException(String.format("Couldn't convert %s", o));
    }
  }
}
