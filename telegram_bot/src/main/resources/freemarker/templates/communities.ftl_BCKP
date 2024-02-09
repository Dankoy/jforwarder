Available communities:
<#list communities as community>
  *community*: ${community.name}
    <#assign sectionNames = community.sections?map(s -> s.name)>
  *sections*: _${sectionNames?join(', ')}_

</#list>
