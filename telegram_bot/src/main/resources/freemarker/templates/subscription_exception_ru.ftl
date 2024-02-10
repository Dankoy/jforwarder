Ожидается полная команда "*${command} {тип_подписки} {аргумент1} {аргумент2}*"

Примеры:

*${command} community music daily*

где:
  _community_ - это тип подписки
  _music_ - это имя сообщества
  _daily_ - это секция сообщества

<#if !command?contains("un")>
Ссылка на подписку - https://coub.com/community/music
</#if>

Или

*${command} tag music popular*

where:
  _tag_ - это тип подписки
  _music_ - это имя тега
  _popular_ - порядок коубов по тегу

<#if !command?contains("un")>
  Ссылка на подписку - https://coub.com/tags/music
</#if>

Чтобы просмотреть доступные порядки по тегам, выполните команду */tag_orders*
Чтобы просмотреть доступные сообщества, выполните команду */communities*

Полный список команд может быть найден /help