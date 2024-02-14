package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.TagSub;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceConflictException;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository.TagSubRepository;

@Service
@RequiredArgsConstructor
public class TagSubServiceImpl implements TagSubService {

    private final TagSubRepository tagSubRepository;
    private final TagService tagService;
    private final ScopeService scopeService;
    private final OrderService orderService;
    private final TypeService typeService;
    private final TelegramChatService telegramChatService;

    @Override
    public Page<TagSub> getAllByActiveTelegramChats(boolean active, Pageable pageable) {
        return tagSubRepository.findAllByChatActive(active, pageable);
    }

    @Override
    public List<TagSub> getAllByTelegramChatId(long telegramChatId) {
        return tagSubRepository.getAllByChatChatId(telegramChatId);
    }

    @Transactional
    @Override
    public TagSub createSubscription(TagSub tagSubscription) {

        // check existence
        var optional =
                tagSubRepository.getByChatChatIdAndTagTitleAndOrderValue(
                        tagSubscription.getChat().getChatId(),
                        tagSubscription.getTag().getTitle(),
                        tagSubscription.getOrder().getValue());

        // if exists throw exception
        optional.ifPresent(
                s -> {
                    throw new ResourceConflictException(
                            String.format(
                                    "Subscription already exists for tag - %s",
                                    tagSubscription.getTag().getTitle()));
                });

        // Throws ResourceNotFoundException
        var tag = tagService.getByTitle(tagSubscription.getTag().getTitle());
        var scope = scopeService.getByName(tagSubscription.getScope().getName());
        var type = typeService.getByName(tagSubscription.getType().getName());
        var order =
                orderService.getByValueAndType(
                        tagSubscription.getOrder().getValue(),
                        tagSubscription.getOrder().getSubscriptionType().getType());

        // todo: do i even need to save chat when creating subscription?

        var optionalChat =
                telegramChatService.getByTelegramChatId(tagSubscription.getChat().getChatId());

        if (optionalChat.isPresent()) {

            var chat = optionalChat.get();

            var newTagSubscription =
                    TagSub.builder()
                            .id(0)
                            .tag(tag)
                            .chat(chat)
                            .order(order)
                            .scope(scope)
                            .type(type)
                            .lastPermalink(null)
                            .build();

            return tagSubRepository.save(newTagSubscription);

        } else {

            var createdChat = telegramChatService.save(tagSubscription.getChat());

            var newTagSubscription =
                    TagSub.builder()
                            .id(0)
                            .tag(tag)
                            .chat(createdChat)
                            .order(order)
                            .scope(scope)
                            .type(type)
                            .lastPermalink(null)
                            .build();

            return tagSubRepository.save(newTagSubscription);
        }
    }

    @Transactional
    @Override
    public void deleteSubscription(TagSub tagSubscription) {

        var optional =
                tagSubRepository.getByChatChatIdAndTagTitleAndOrderValue(
                        tagSubscription.getChat().getChatId(),
                        tagSubscription.getTag().getTitle(),
                        tagSubscription.getOrder().getValue());

        optional.ifPresent(tagSubRepository::delete);
    }
}
