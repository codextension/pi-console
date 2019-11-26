# ./bin/spark-submit --master local --packages org.apache.spark:spark-streaming-kafka_2.11:1.6.3 run.py

import sys
from pyspark import SparkContext
from pyspark.streaming import StreamingContext
from pyspark.streaming.kafka import KafkaUtils

if __name__ == "__main__":
    sc = SparkContext(appName="TestApp")
    ssc = StreamingContext(sc, 2)
    kvs = KafkaUtils.createDirectStream(ssc, ["temperature"], {"metadata.broker.list":"localhost:9092"})
    
    lines = kvs.map(lambda x: x[1])
    counts = lines.flatMap(lambda line: line.split(" ")).map(lambda word: (word, 1)).reduceByKey(lambda a, b: a+b)
    counts.pprint()
    ssc.start()
    ssc.awaitTermination()