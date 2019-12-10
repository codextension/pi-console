# ./bin/spark-submit --master local --packages org.apache.spark:spark-streaming-kafka_2.11:1.6.3 run.py

import sys
from pyspark import SparkContext, SQLContext
from pyspark.streaming import StreamingContext
from pyspark.streaming.kafka import KafkaUtils
import json

if __name__ == "__main__":

    sc = SparkContext(appName="TestApp")
    ssc = StreamingContext(sc, 60)
    kvs = KafkaUtils.createDirectStream(ssc, ["temperature"], {"metadata.broker.list":"localhost:9092"}, valueDecoder=lambda m: json.loads(m.decode('utf-8')))
    sqlContext = SQLContext(sc)

    avg_by_key = kvs.map(lambda x : (x[0], x[1]['temperature'])).groupByKey().map(lambda x : (list(x[1])))
    avg_by_key.pprint()

    ssc.start()
    ssc.awaitTermination()
