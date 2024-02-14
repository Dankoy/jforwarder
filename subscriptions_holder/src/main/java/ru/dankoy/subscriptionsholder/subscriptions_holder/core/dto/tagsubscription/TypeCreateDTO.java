package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.tagsubscription;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.Type;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TypeCreateDTO {

    private String name;

    public static TypeCreateDTO toDTO(Type type) {

        return new TypeCreateDTO(type.getName());
    }

    public static Type fromDTO(TypeCreateDTO dto) {

        return new Type(0, dto.getName());
    }
}
