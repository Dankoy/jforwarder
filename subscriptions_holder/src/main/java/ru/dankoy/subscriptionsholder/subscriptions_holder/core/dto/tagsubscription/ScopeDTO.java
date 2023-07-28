package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.tagsubscription;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.tag.Scope;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ScopeDTO {

  private long id;

  private String name;

  public static ScopeDTO toDTO(Scope scope) {

    return new ScopeDTO(
        scope.getId(),
        scope.getName()
    );

  }

  public static Scope fromDTO(ScopeDTO dto) {

    return new Scope(
        dto.getId(),
        dto.getName()
    );

  }


}
