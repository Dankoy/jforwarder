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

Или

  *${command} channel meteoralp oldest*

where:
  _channel_ - это тип подписки
  _meteoralp_ - идентификатор канала
  _oldest_ - порядок коубов по каналу

<#if !command?contains("un")>
  Ссылка на подписку - https://coub.com/meteoralp
  Порядок кубов по количеству лайков

  Идентификатор канала можно найти в URL канала. Для этого открой интересуемый канал в браузере
  и скопируй последнее слово из URL.

  Например, для канала *Meteora* идентификатор канала - *meteoralp*.
  Ссылка на канал https://coub.com/meteoralp

</#if>

Чтобы просмотреть доступные порядки по тегам, выполните команду */orders tag*
Чтобы просмотреть доступные порядки по каналам, выполните команду */orders channel*
Чтобы просмотреть доступные сообщества, выполните команду */communities*

Полный список команд может быть найден /help