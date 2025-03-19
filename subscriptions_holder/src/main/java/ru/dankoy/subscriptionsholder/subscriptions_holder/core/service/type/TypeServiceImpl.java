package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.type;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.Type;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceNotFoundException;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository.type.TypeRepository;

@Service
@RequiredArgsConstructor
public class TypeServiceImpl implements TypeService {

  private final TypeRepository typeRepository;

  @Override
  public Type getByName(String name) {
    var optional = typeRepository.findByName(name);

    return optional.orElseThrow(
        () -> new ResourceNotFoundException(String.format("Tag type not found - %s", name)));
  }
}
