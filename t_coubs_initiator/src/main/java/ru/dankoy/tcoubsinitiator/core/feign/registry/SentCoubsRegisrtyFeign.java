package ru.dankoy.tcoubsinitiator.core.feign.registry;

import java.time.LocalDateTime;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.registry.SentCoubsRegistry;

@FeignClient(contextId = "registry", name = "subscriptions-holder")
public interface SentCoubsRegisrtyFeign {
    @GetMapping(
            path = "/api/v1/sent_coubs_registry",
            params = {"page", "size", "sort"})
    Page<SentCoubsRegistry> getAll();

    @GetMapping(
            path = "/api/v1/sent_coubs_registry",
            params = {"subscriptionId", "page", "size", "sort"})
    Page<SentCoubsRegistry> getAllBySubscriptionId(
            @RequestParam long subscriptionId, Pageable pageable);

    @GetMapping(
            path = "/api/v1/sent_coubs_registry",
            params = {"subscriptionId", "dateTime", "page", "size", "sort"})
    Page<SentCoubsRegistry> getAllBySubscriptionIdAndDateAfter(
            @RequestParam long subscriptionId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    LocalDateTime dateTime,
            Pageable pageable);

    @PostMapping(path = "/api/v1/sent_coubs_registry")
    SentCoubsRegistry create(@RequestBody SentCoubsRegistry sentCoubsRegistry);

    @DeleteMapping(path = "/api/v1/sent_coubs_registry/{id}")
    void delete(@PathVariable long id);
}
