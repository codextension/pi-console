# ./bin/spark-submit --master local --packages org.apache.spark:spark-streaming-kafka_2.11:1.6.3 run.py

import sys
from pyspark import SparkContext
from pyspark.streaming import StreamingContext
from pyspark.streaming.kafka import KafkaUtils
import json

if __name__ == "__main__":

    sc = SparkContext(appName="TestApp")
    ssc = StreamingContext(sc, 60)
    kvs = KafkaUtils.createDirectStream(ssc, ["dust"], {"metadata.broker.list":"localhost:9092"}, valueDecoder=lambda m: json.loads(m.decode('utf-8')))

    avg_by_key = kvs.groupByKey()
    avg_by_key.pprint()

    ssc.start()
    ssc.awaitTermination()