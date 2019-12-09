# ./bin/spark-submit --master local --packages org.apache.spark:spark-streaming-kafka_2.11:1.6.3 run.py

import sys
from pyspark import SparkContext
from pyspark.streaming import StreamingContext
from pyspark.streaming.kafka import KafkaUtils

if __name__ == "__main__":

    def avg_map_func(row):
        return (row[0], (row[2], 1))

    def avg_reduce_func(value1, value2):
        return ((value1[0] + value2[0], value1[1] + value2[1])) 

    sc = SparkContext(appName="TestApp")
    ssc = StreamingContext(sc, 60)
    kvs = KafkaUtils.createDirectStream(ssc, ["dust"], {"metadata.broker.list":"localhost:9092"})

    avg_by_key = kvs.map(avg_map_func).reduceByKey(avg_reduce_func).mapValues(lambda x: x[0]/x[1])
    avg_by_key.pprint()

    ssc.start()
    ssc.awaitTermination()