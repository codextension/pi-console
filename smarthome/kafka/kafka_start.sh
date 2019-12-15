#!/bin/bash
# remember to update  the server.properties, and open the ports
cd ~/kafka_2.12-2.3.0
echo "Starting zookeeper ..."
nohup bin/zookeeper-server-start.sh config/zookeeper.properties > logs/_zookeeper.log &
sleep 5
echo "Starting kafka server ..."
nohup bin/kafka-server-start.sh config/server.properties > logs/_kafka.log &
sleep 5
echo "creating all 3 topics ..."
bin/kafka-topics.sh --create --bootstrap-server 192.168.178.63:9092 --replication-factor 1 --partitions 1 --topic temperature
bin/kafka-topics.sh --create --bootstrap-server 192.168.178.63:9092 --replication-factor 1 --partitions 1 --topic dust
bin/kafka-topics.sh --create --bootstrap-server 192.168.178.63:9092 --replication-factor 1 --partitions 1 --topic noise
sleep 5
echo "The following topics are created:"
bin/kafka-topics.sh --list --bootstrap-server 192.168.178.63:9092
echo "Done ..."


