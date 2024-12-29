package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.searchparser;

import java.util.List;

import ru.dankoy.subscriptionsholder.subscriptions_holder.core.specifications.telegramchat.criteria.SearchCriteria;

public interface SearchCriteriaParser {

    List<SearchCriteria> parse(String search);

}
