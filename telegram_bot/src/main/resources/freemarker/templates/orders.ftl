Available orders:
<#list orders as order>
  *${order?counter}*: ${order.value}
</#list>
