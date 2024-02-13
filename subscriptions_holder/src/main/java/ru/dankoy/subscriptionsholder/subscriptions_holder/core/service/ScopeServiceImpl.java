
package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.Scope;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceNotFoundException;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository.ScopeRepository;


@Service
@RequiredArgsConstructor
public class ScopeServiceImpl implements ScopeService {

  private final ScopeRepository scopeRepository;

  @Override
  public Scope getByName(String name) {
    var optional = scopeRepository.findByName(name);

    return optional.orElseThrow(
        () -> new ResourceNotFoundException(String.format("Tag scope not found - %s", name))
    );
  }
}
