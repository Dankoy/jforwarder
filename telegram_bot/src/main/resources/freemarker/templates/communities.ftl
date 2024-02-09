Available communities:

<#assign communityNames = communities?map(community -> community.name)>
<#list communityNames as cn>
    ${cn?counter}: *${cn}*
</#list>

Available sections for *each* community:

<#assign sectionNames = communities?first.sections?map(s -> s.name)>
<#list sectionNames as sn>
    ${sn?counter}: _${sn}_
</#list>

