package ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.Section;

@RepositoryRestResource(path = "sections")
public interface SectionRepository extends JpaRepository<Section, Long> {

  Optional<Section> getByName(String name);

}
