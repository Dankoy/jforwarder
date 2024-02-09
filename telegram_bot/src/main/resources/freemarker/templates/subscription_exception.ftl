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

To see available tag orders run */tag_orders* command
To see available communities and its sections run */communities* command

Full list of commands can be found in /help