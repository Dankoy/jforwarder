Expected full command "*${command} {subscription_type} {field1} {field2}*"

For example:

*${command} community music daily*

where:
  _community_ - is the type of subscription
  _music_ - the name of community
  _daily_ - community section

<#if !command?contains("un")>
Link to subscription - https://coub.com/community/music
</#if>

Or

  *${command} tag music popular*

where:
  _tag_ - is the type of subscription
  _music_ - the name of tag
  _popular_ - coub tag ordering

<#if !command?contains("un")>
Link to subscription - https://coub.com/tags/music
</#if>

Or

  *${command} channel meteoralp oldest*

where:
  _channel_ - is the type of subscription
  _meteoralp_ - the permalink of channel
  _oldest_ - coub channel ordering

<#if !command?contains("un")>
  Link to subscription - https://coub.com/meteoralp
  Order by most liked coubs

  Channel id can be found in the URL leading to channel. Just open interested channel in browser
  and copy last word from URL.

  For example, for channel *Meteora*, id is - *meteoralp*
  Link to channel - https://coub.com/meteoralp
</#if>

To see available tag orders run */orders tag* command
To see available channel orders run */orders channel* command
To see available communities and its sections run */communities* command

Full list of commands can be found in /help