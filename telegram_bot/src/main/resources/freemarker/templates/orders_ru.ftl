Доступные порядки:
<#list orders as order>
  *${order?counter}*: ${order.value}
</#list>
