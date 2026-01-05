package ru.dankoy.telegrambot.core.domain.page;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.ArrayNode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RestPageImpl<T> extends PageImpl<T> {
  @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
  public RestPageImpl(
      @JsonProperty("content") List<T> content,
      @JsonProperty("number") int number,
      @JsonProperty("size") int size,
      @JsonProperty("totalElements") Long totalElements,
      @JsonProperty("pageable") JsonNode pageable,
      @JsonProperty("last") boolean last,
      @JsonProperty("totalPages") int totalPages,
      @JsonProperty("sort") JsonNode sort,
      @JsonProperty("numberOfElements") int numberOfElements) {
    super(content, PageRequest.of(number, size, sortParse(sort)), totalElements);
  }

  private static Sort sortParse(JsonNode sort) {

    List<Order> orders = new ArrayList<>();

    if (sort.isArray()) {
      ArrayNode arrayField = (ArrayNode) sort;
      arrayField.forEach(
          node -> {
            var direction = node.get("direction").asString();
            var property = node.get("property").asString();
            if (direction.equals("ASC")) {
              orders.add(Order.asc(property));
            } else {
              orders.add(Order.desc(property));
            }
          });
    }

    return Sort.by(orders);
  }
}
