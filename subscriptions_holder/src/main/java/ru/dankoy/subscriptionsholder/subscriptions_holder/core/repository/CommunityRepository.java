package ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository;

import feign.Param;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.community.Community;

public interface CommunityRepository extends JpaRepository<Community, Long> {

  @EntityGraph(value = "sections-entity-graph")
  @Override
  List<Community> findAll();

  @EntityGraph(value = "sections-entity-graph")
  Optional<Community> getByName(String name);

  @EntityGraph(value = "sections-entity-graph")
  Optional<Community> getByNameAndSectionsName(String name, String sectionName);

  @Query(
      """
              select c from Community c
              join c.sections s
              where s.name in :sectionNames
              and c.name = :name
          """)
  Optional<Community> getByNameAndSections(
      @Param("name") String name, @Param("sectionNames") Set<String> sectionNames);
}
