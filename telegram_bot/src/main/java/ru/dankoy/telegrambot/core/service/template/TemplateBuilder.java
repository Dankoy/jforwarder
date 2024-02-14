package ru.dankoy.telegrambot.core.service.template;

import java.util.Locale;
import java.util.Map;

public interface TemplateBuilder {

    String writeTemplate(Map<String, Object> templateData, String templateName);

    String writeTemplate(Map<String, Object> templateData, String templateName, Locale locale);

    String loadTemplateFromString(
            String templateName, String templateString, Map<String, Object> templateData);
}
