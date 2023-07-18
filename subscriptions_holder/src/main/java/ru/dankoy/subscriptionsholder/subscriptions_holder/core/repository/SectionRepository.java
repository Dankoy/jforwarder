package ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.Section;

public interface SectionRepository extends JpaRepository<Section, Long> {

  Optional<Section> getByName(String name);

  List<Section> getByNameIsIn(Set<String> names);

}
