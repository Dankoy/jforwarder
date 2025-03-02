package ru.dankoy.subscriptionsholder.subscriptions_holder.core.jpaconverter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Objects;
import java.util.UUID;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.JpaConverterException;

@Converter
public class JpaStringToUuidConverter implements AttributeConverter<UUID, String> {

  @Override
  public String convertToDatabaseColumn(UUID attribute) {
    checkNull(attribute);

    return attribute.toString();
  }

  @Override
  public UUID convertToEntityAttribute(String dbData) {

    if (Objects.isNull(dbData)) {
      return null;
    } else {
      return UUID.fromString(dbData);
    }
  }

  private void checkNull(Object o) {
    if (Objects.isNull(o)) {
      throw new JpaConverterException(String.format("Couldn't convert %s", o));
    }
  }
}
