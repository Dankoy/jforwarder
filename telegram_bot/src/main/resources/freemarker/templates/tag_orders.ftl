Available tag orders:
<#list orders as order>
  *${order?counter}*: ${order.name}
</#list>
