package ru.dankoy.telegrambot.core.httpservice.argumentresolver;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.service.invoker.HttpRequestValues;
import org.springframework.web.service.invoker.HttpServiceArgumentResolver;

public class PageableArgumentResolver implements HttpServiceArgumentResolver {

  @Override
  public boolean resolve(
      Object argument, MethodParameter parameter, HttpRequestValues.Builder requestValues) {

    if (!parameter.getParameterType().equals(Pageable.class)) {
      return false;
    }

    if (argument != null) {
      var pageable = (Pageable) argument;

      if (pageable.isPaged()) {
        requestValues.addRequestParameter("page", String.valueOf(pageable.getPageNumber()));
        requestValues.addRequestParameter("size", String.valueOf(pageable.getPageSize()));
      }

      Sort sort = pageable.getSort();
      if (sort.isSorted()) {
        for (Sort.Order order : sort) {
          requestValues.addRequestParameter(
              "sort", order.getProperty() + "," + order.getDirection().name());
        }
      }
    }

    return true;
  }
}
