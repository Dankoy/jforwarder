package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.tagsubscription;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.Scope;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ScopeCreateDTO {

  private String name;

  public static ScopeCreateDTO toDTO(Scope scope) {

    return new ScopeCreateDTO(
        scope.getName()
    );

  }

  public static Scope fromDTO(ScopeCreateDTO dto) {

    return new Scope(
        0,
        dto.getName()
    );

  }


}
