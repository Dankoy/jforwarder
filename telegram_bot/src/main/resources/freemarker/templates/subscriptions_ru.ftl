<#if communitySubscriptions?has_content || tagSubscriptions?has_content || channelSubscriptions?has_content>
    <#if communitySubscriptions?has_content>
    Подписки на сообщества:
        <#list communitySubscriptions as communitySubscription>
          ${communitySubscription?counter}: ${communitySubscription.community.name} ${communitySubscription.section.name}
        </#list>
    </#if>

    <#if tagSubscriptions?has_content>
  Подписки на теги
        <#list tagSubscriptions as tagSubscription>
          ${tagSubscription?counter}: ${tagSubscription.tag.title} ${tagSubscription.order.value}
        </#list>
    </#if>

    <#if channelSubscriptions?has_content>
  Подписки на каналы
        <#list channelSubscriptions as channelSubscription>
            ${channelSubscription?counter}: ${channelSubscription.channel.title} ${channelSubscription.order.value}
        </#list>
    </#if>
<#else>
  Подписок нет. Посмотри на примеры /subscribe команды.
</#if>



