package ru.dankoy.telegrambot.core.service.template;

import java.util.Map;

public interface TemplateBuilder {

  String NETBOX_TEMPLATE_DIR = "/templates/netbox";

  String writeTemplate(Map<String, Object> templateData, String templateName);

  String loadTemplateFromString(String templateName, String templateString, Map<String,
      Object> templateData);

}
