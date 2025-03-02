package ru.dankoy.telegramchatservice.core.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.dankoy.telegramchatservice.core.domain.dto.ChatWithSubs;
import ru.dankoy.telegramchatservice.core.domain.search.RegexSearchCriteria;

/**
 * @deprecated because DDD and microservice separation. For working example see subscription_holder
 *     microservice
 */
public interface TelegramChatRepositoryCustom {

  @Deprecated(since = "2025-02-25")
  Page<ChatWithSubs> findAllWithSubsBy(Pageable pageable);

  @Deprecated(since = "2025-02-25")
  Page<ChatWithSubs> findAllWithSubsByCriteria(List<RegexSearchCriteria> search, Pageable pageable);
}
