package ru.dankoy.telegrambot.core.dto.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import org.telegram.telegrambots.meta.api.methods.ParseMode;

public class ParseModeValidator implements ConstraintValidator<ParseModeConstraint, String> {
    @Override
    public boolean isValid(
            String parseMode, ConstraintValidatorContext constraintValidatorContext) {
        return Objects.nonNull(parseMode)
                && (parseMode.equals(ParseMode.MARKDOWN)
                        || parseMode.equals(ParseMode.MARKDOWNV2)
                        || parseMode.equals(ParseMode.HTML));
    }
}
