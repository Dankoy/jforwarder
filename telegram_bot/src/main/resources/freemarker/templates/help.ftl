Here you can subscribe to coub.com communities and/or tags and receive updates directly in chat.

Commands references:

1) /my_subscriptions - returns all your active subscriptions in chat or group chat

2) /subscribe {subscription_type} {field1} {field2}

  where:
    {subscription_type} - is the type of subscription. Could be: ${subscription_types?lower_case}.
    Contents of {field1} and {field2} depend on subscription_type. Please refer to example below.
    To see available tag orders run /orders command
    To see available communities run /communities command

3) /unsubscribe {subscription_type} {field1} {field2}
    
  where:
    {subscription_type} - is the type of subscription. Could be: ${subscription_types?lower_case}.
    Contents of {field1} and {field2} depend on subscription_type. Please refer to example below.
    To see available tag orders run /orders command
    To see available communities run /communities command

4) /orders - shows available orders for tag subscriptions
    {field1} - один из ${subscription_types?lower_case}

5) /communities - shows available communities and sections for community subscriptions

---

Subscription example:

1) Subscribe to community:
  /subscribe community cars weekly

2) Subscribe to tag:
  /subscribe tag cars newest

3) Subscribe to tag containing multiple words:
  /subscribe tag dodge charger newest
    dodge charger - is name of tag