# ./bin/spark-submit --master local --packages org.apache.spark:spark-streaming-kafka_2.11:1.6.3 run.py

import sys
from pyspark import SparkContext
from pyspark.streaming import StreamingContext
from pyspark.streaming.kafka import KafkaUtils

if __name__ == "__main__":
    sc = SparkContext(appName="TestApp")
    ssc = StreamingContext(sc, 60)
    kvs = KafkaUtils.createDirectStream(ssc, ["temperature"], {"metadata.broker.list":"localhost:9092"})
    
    avg_by_key = kvs.mapValues(lambda v: (v, 1)).reduceByKey(lambda a,b: (a[0]+b[0], a[1]+b[1])).mapValues(lambda v: v[0]/v[1])
    avg_by_key.pprint()

    ssc.start()
    ssc.awaitTermination()
    avg_by_key = kvs.mapValues(lambda v: (v, 1)).reduceByKey(lambda a,b: (a[0]+b[0], a[1]+b[1])).mapValues(lambda v: v[0]/v[1])
    avg_by_key.pprint()

    ssc.start()
    ssc.awaitTermination()