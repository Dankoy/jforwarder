package ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.Community;

@RepositoryRestResource(path = "communities")
public interface CommunityRepository extends JpaRepository<Community, Long> {

  Optional<Community> getByName(String name);

}
