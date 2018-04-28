## Introduction
This is an application that read the files from a specified folder and builds the direct and indirect index from these files. The indexes are stored in a MongoDB database. 

The app can be run sequentially using the class App found in the fac package. Running the app in an distributed environment is a bit more difficult. You have to run the kafka container, set up the topic and run the reducers and dispatcher apps found in the src folders. The dispatcher reads the input directory and dispatches the work to the other processes. The reducers can be horizontally scaled. You can have a single one of each reducer or multiple distributed across multiple machines or different processes. 

Multiple classes are being used from the tools package. Words are being stemmed using the Porter stemmer from the library Apache OpenNlp. Classes are being provided for bool searching and for vector searching through multiple documents found in the database.


### How to use kafka with docker

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

### Kafka info

Partitions throughput 10 MB / s

Partitions per topic: 1 is enough

Replication factor: usually 3 (needs 3 brokers), 1 in our case

### Kafka topics

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

### MongoDB
I use a premade docker container with MongoDB for simplicity.

```
docker run -p 27017:27017 --name ecbd-mongo -d mongo
docker run -it --link ecbd-mongo:mongo --rm mongo sh -c 'exec mongo "$MONGO_PORT_27017_TCP_ADDR:$MONGO_PORT_27017_TCP_PORT/test"'
```