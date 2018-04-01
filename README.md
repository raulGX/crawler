# crawler

## How to use kafka with docker

Start the vm

```
docker run --rm -it \
  -p 2181:2181 -p 3030:3030 -p 8081:8081 \
  -p 8082:8082 -p 8083:8083 -p 9092:9092 \
  -e ADV_HOST=127.0.0.1 \
  landoop/fast-data-dev
```

Kafka ui: http://127.0.0.1:3030

Connect to kafka vm using bash

```
docker run --rm -it --net=host landoop/fast-data-dev bash
```

## Kafka info

Partitions throughput 10 MB / s

Partitions per topic: 1 is enough

Replication factor: usually 3 (needs 3 brokers), 1 in our case

## Kafka topics

```
kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 3 --topic doc-to-process
kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 3 --topic direct-index
kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 3 --topic split-direct-index

//configs
--config cleanup.policy=delete
```

Number of partions: 1 for every desired reader (3 consumer vms - 3 partitions)

Add partitions to existing topic:
```
kafka-topics --zookeeper localhost:2181 --alter --topic doc-to-process --partitions 3`
```