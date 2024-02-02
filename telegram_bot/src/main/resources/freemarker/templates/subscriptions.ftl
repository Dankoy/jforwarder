<#if communitySubscriptions?has_content || tagSubscriptions?has_content>
    <#if communitySubscriptions?has_content>
    Community subscriptions:
        <#list communitySubscriptions as communitySubscription>
          ${communitySubscription?counter}: ${communitySubscription.community.name} ${communitySubscription.section.name}
        </#list>
    </#if>

    <#if tagSubscriptions?has_content>
  Tag subscriptions
        <#list tagSubscriptions as tagSubscription>
          ${tagSubscription?counter}: ${tagSubscription.tag.title} ${tagSubscription.order.value}
        </#list>
    </#if>
<#else>
  You are not subscribed to anything. Check /help command.
</#if>



