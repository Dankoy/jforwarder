# Naming is crucial

The bean neaming is very important.

ConsumerFactory different from ConsumerFactory<Object, Object> should have bean name different from
consumerFactory. Because spring boot creates bean with name consumerFactory behind the scenes. And
your bean with same name makes unresolvable circular dependencies.

Same with KafkaListenerContainerFactory. Custom bean shall be named different from
kafkaListenerContainerFactory.

# ConsumerContainerFactory

This factory when bound to listener actually creates separate objects. So you can have one factory
with concurrency = 2. And bind this factory to two different listeners. That means, for each
listener spring will create separate instance of container listener with two concurrency = 2. So in
real two listeners. Could be configured in annotation via concurrency attribute, or in factory via
setConcurrency method.

# Multithreading consumers

That's not how Apache Kafka works. An idea there is always process records in the same partition in
a single thread. That factory.setConcurrency(5); is definitely around how many partitions you have
in a topic. So, if you have only one, this property doesn't bring any value. If you have 10
partitions in the topic, then Spring Kafka spawns 5 threads and each of them is going to handle 2
partition.

So, if you would like to have such a concurrency you describe, you indeed have to create 5
partitions in your topic. And only after that you will be able to process records in the same topic
in parallel.

# The spring boot approach

Forget about container factories. Only use @KafkaListener with annotation parameters.

For more precise configuration one has to use pure spring approach. That means one has to create and
custom configure container factories

# MessageListenerContainer

Two MessageListenerContainer implementations are provided:

    KafkaMessageListenerContainer
    ConcurrentMessageListenerContainer

The KafkaMessageListenerContainer receives all message from all topics or partitions on a single
thread. The ConcurrentMessageListenerContainer delegates to one or more
KafkaMessageListenerContainer instances to provide multi-threaded consumption.

# RecordFilterStrategy

Kafka can filter by object type but NOT with batch mode.

If you want to filter messages by type using filter strategy, you should either create multiple
KafkaListenerContainerFactory or create filter strategy and use them in kafka listener annotation

RecordFilterStrategy are added in listener container factory. So custom factories should be
applied.

```java

// THE NOT policy. So if such messages that apply to this lambda are found
// They will be discarded.

@Bean
public RecordFilterStrategy<String, CoubMessage> recordFilterStrategyByCommunity() {
  return r -> Arrays.toString(r.headers().lastHeader("subscription_type").value())
      .equals("BY_COMMUNITY");
}

@Bean
public RecordFilterStrategy<String, TagSubscriptionMessage> recordFilterStrategyByTag() {
  return r -> Arrays.toString(r.headers().lastHeader("subscription_type").value()).equals("BY_TAG");
}
```

# Listener

Every listener in same group can listen its partition.
If there are 1 partition and two listeners, then one listener won't work.
If i want to have two listeners that reads the same topic and have filter by type in them. Then
these listeners have to be in different groups

# ConcurrentMessageListenerContainer

When listening to multiple topics, the default partition distribution may not be what you expect.
For example, if you have three topics with five partitions each and you want to use concurrency=15,
you see only five active consumers, each assigned one partition from each topic, with the other 10
consumers being idle. This is because the default Kafka PartitionAssignor is the RangeAssignor (see
its Javadoc). For this scenario, you may want to consider using the RoundRobinAssignor instead,
which distributes the partitions across all of the consumers. Then, each consumer is assigned one
topic or partition. To change the PartitionAssignor, you can set the partition.assignment.strategy
consumer property (ConsumerConfigs.PARTITION_ASSIGNMENT_STRATEGY_CONFIG) in the properties provided
to the DefaultKafkaConsumerFactory.

When using Spring Boot, you can assign set the strategy as follows:

spring.kafka.consumer.properties.partition.assignment.strategy=\
org.apache.kafka.clients.consumer.RoundRobinAssignor