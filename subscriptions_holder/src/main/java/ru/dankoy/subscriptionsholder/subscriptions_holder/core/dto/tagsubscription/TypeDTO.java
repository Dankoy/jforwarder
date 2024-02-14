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
public class TypeDTO {

    private long id;

    private String name;

    public static TypeDTO toDTO(Type type) {

        return new TypeDTO(type.getId(), type.getName());
    }

    public static Type fromDTO(TypeDTO dto) {

        return new Type(dto.getId(), dto.getName());
    }
}
