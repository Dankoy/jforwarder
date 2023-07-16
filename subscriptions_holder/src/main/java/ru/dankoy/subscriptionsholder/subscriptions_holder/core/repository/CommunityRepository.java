package ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.Community;

@RepositoryRestResource(path = "communities")
public interface CommunityRepository extends JpaRepository<Community, Long> {

  List<Community> getByName(String name);

  Optional<Community> getByNameAndSectionName(String name, String sectionName);

}
