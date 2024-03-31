# Overview
* Kinesis brings real-time computation nearest the source of origin where events in the world are captured and of most value.
* Kinesis helps you get move towards making preventive/predictable actions because it is much closer to the datasource. 
Compare this to traditional batch which is delayed by a seconds/minutes/hours/days+ time frame which makes them more 
reactive and historical. This difference makes lets the system provide real-time analytics and makes it event driven.
* When comparing sqs-sns to kinesis, consider  whether the streams are related to each other, message retention, multiple 
consumers picking up the same message concurrently, or message ordering.

# Kinesis Data Streams
* pub/sub style messaging system for real-time data events
* Desire to decouple system components to increase reliability, scalability, and general performance
  * This is dependent on a standard data format
* A stream is an abstraction for grouping messages of similar data structure. Streams are composed of scalable units of 
parallelism called shards. This can lead to "hot shards" where 1 shard gets too much load. Charges are per shard.
* Producers write data to a stream to be used by downstream consumers
* Data retention of 24 hours up to 1 year.
* A data record in Kinesis is composed of a sequence number, partition key, and blob.
* Partition keys are used to group records in the same shard and allow for consumption in the order they were written
based on sequence number.
  * ![streams-shards-records.png](diagrams%2Fstreams-shards-records.png)
* Shards have limits on how much they can read and write. 
  * Write up to 1MB/s. 1,000 records written/s with max individual payload of 1 MB.
  * Read up to 2MB/s. 10,000 records read/s. Max of 5 reads/s. Max individual read of 10MB with 5s timeout at capacity.
  * Enhanced fanout consumers  use a model of consumption where data is pushed to consumers to get around this (2MB/s/consumer).
* Depending on sdk `PutRecord` and `PutRecords`(batching) are the preferred approach.
* Sequence number can guarantee ordering for a shard.
* Kinesis Producer Library is a java specific library optimized for high-throughput producing kinesis streams.
* Two general types of kinesis consumers: Pull (Traditional) & Push (Enhanced Fanout)
  * Pull: ListShards, GetShardIterator, GetRecords
  * Push: RegisterStreamConsumer, SubscribeToShard (ParallelizationFactor to scale, MaxBatchingWindow to control throughput)
  * 