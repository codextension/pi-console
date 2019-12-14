import findspark
findspark.init("/home/elie/Applications/spark-2.4.4-bin-hadoop2.7")
findspark.find()

from pyspark.sql import SparkSession
from pyspark.sql.functions import explode
from pyspark.sql.functions import split
from pyspark.sql.types import * 
from pyspark.sql.functions import *

if __name__ == "__main__":
    spark = SparkSession.builder.appName("StructuredNetworkWordCount").getOrCreate()
    df = spark.readStream.format('kafka').option("kafka.bootstrap.servers", "192.168.178.63:9092").option("value.deserializer","lambda m: json.loads(m.decode('utf-8'))").option("subscribe", "temperature").load()
    df.selectExpr("CAST(key AS STRING) AS key", "to_json(struct(*)) as value")
    df.printSchema()

    df.writeStream.format("console").option(
        "truncate", "false").start().awaitTermination()
