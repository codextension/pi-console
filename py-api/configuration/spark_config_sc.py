# ./bin/spark-submit --master local --packages org.apache.spark:spark-streaming-kafka_2.11:1.6.3 run.py

import findspark
findspark.init("/home/elie/Applications/spark-2.4.4-bin-hadoop2.7")
findspark.find()


import sys
from pyspark import SparkContext, SQLContext
from pyspark.streaming import StreamingContext
from pyspark.streaming.kafka import KafkaUtils
import json

if __name__ == "__main__":

    sc = SparkContext(appName="TestApp")
    ssc = StreamingContext(sc, 20)
    kvs = KafkaUtils.createDirectStream(ssc, ["temperature"], {"metadata.broker.list":"192.168.178.63:9092"}, valueDecoder=lambda m: json.loads(m.decode('utf-8')))
    sqlContext = SQLContext(sc)

    avg_by_key = kvs.groupByKey().map(lambda x : (list(x[1])))
    avg_by_key.pprint()

    ssc.start()
    ssc.awaitTermination()
