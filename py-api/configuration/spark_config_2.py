import findspark

findspark.init("/home/elie/Applications/spark-2.4.4-bin-hadoop2.7")
findspark.find()

from pyspark.sql import SparkSession
from pyspark.sql.functions import explode
from pyspark.sql.functions import split
from pyspark.sql.types import *
from pyspark.sql.functions import *

if __name__ == "__main__":
    temp_schema = StructType(
        [
            StructField("value", IntegerType(), True),
            StructField("voltage", FloatType(), True),
            StructField("density", DoubleType() , True),
            StructField("timestamp", DoubleType() , True)
        ]
    )

    spark = SparkSession.builder.appName("dust_parser").getOrCreate()
    df = (
        spark.readStream.format("kafka")
        .option("kafka.bootstrap.servers", "192.168.178.63:9092")
        .option("subscribe", "dust")
        .option("startingOffsets", "earliest")
        .load()
    )

    sensor = df.withColumn("value", df.value.astype("string"))
    sensor = sensor.select(from_json(sensor.value, temp_schema).alias("data")).select("data.*")
    sensor = sensor.withColumn("timestamp", sensor["timestamp"].cast(TimestampType()))

    sensor = (
        sensor.withWatermark("timestamp", "5 minutes")
        .groupBy(window("timestamp", "2 minutes", "1 minutes"))
        .avg("value")
    )

    sensor.printSchema()
    sensor.writeStream.format("console").outputMode("append").start().awaitTermination()
