package ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.channel.Channel;

public interface ChannelRepository extends JpaRepository<Channel, Long> {

    Optional<Channel> getByTitle(String title);

    Optional<Channel> getByPermalink(String permalink);
}
