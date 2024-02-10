Доступные сообщества:

<#assign communityNames = communities?map(community -> community.name)>
<#list communityNames as cn>
    ${cn?counter}: *${cn}*
</#list>

Доступные секции для *каждого* сообщества:

<#assign sectionNames = communities?first.sections?map(s -> s.name)>
<#list sectionNames as sn>
    ${sn?counter}: _${sn}_
</#list>

