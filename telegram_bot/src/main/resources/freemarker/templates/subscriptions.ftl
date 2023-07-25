<#list subscriptions as subscription>
*${subscription?counter}*: _${subscription.community.name} ${subscription.section.name}_
</#list>