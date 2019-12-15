import findspark
import os
from pyspark.sql import SparkSession
from pyspark.sql.functions import explode
from pyspark.sql.functions import split
from pyspark.sql.types import *
from pyspark.sql.functions import *

# Initialising pyspark (the spark runner)
packages = [
    "com.h2database:h2:1.4.200",
    "com.alibaba:fastjson:1.2.62",
    "org.apache.spark:spark-streaming-kafka_2.11:1.6.3",
    "org.apache.spark:spark-sql-kafka-0-10_2.11:2.4.4",
    "org.apache.logging.log4j:log4j-api:2.7",
    "org.apache.logging.log4j:log4j-core:2.7",
    "org.apache.spark:spark-core_2.11:2.4.4",
    "org.apache.spark:spark-sql_2.11:2.4.4",
]

os.environ[
    "PYSPARK_SUBMIT_ARGS"
] = f"--master local --packages {','.join(packages)}  pyspark-shell"
findspark.init("/home/elie/Applications/spark-2.4.4-bin-hadoop2.7")
findspark.find()

# Reading the dust information from kafka, calculating thte average on a 1 minute window, then storing in an h2 database.
if __name__ == "__main__":
    temp_schema = StructType(
        [
            StructField("value", IntegerType(), True),
            StructField("voltage", FloatType(), True),
            StructField("density", DoubleType(), True),
            StructField("timestamp", DoubleType(), True),
        ]
    )

    def write_jdbc(df, epoch_id):
        # Transform and write batchDF
        df.persist()
        df = df.withColumn(
            "id", monotonically_increasing_id()
        )  # adding a db identifier

        df.write.format("jdbc").mode("append").options(
            url="jdbc:h2:~/pi",
            dbtable="dust",
            driver="org.h2.Driver",
            user="sa",
            password="sa",
        ).save()
        df.unpersist()

    spark = SparkSession.builder.appName("dust_parser").getOrCreate()
    df = (
        spark.readStream.format("kafka")
        .option("kafka.bootstrap.servers", "192.168.178.63:9092")
        .option("subscribe", "dust")
        .option("startingOffsets", "latest")
        .load()
    )

    sensor = df.withColumn("value", df.value.astype("string"))
    sensor = sensor.select(from_json(sensor.value, temp_schema).alias("data")).select(
        "data.*"
    )
    
    # converting timestamp column to Timestamp type, somehow the conversion doesn't work from the start.
    sensor = sensor.withColumn("timestamp", sensor["timestamp"].cast(TimestampType()))
    
    sensor = (
        sensor.withWatermark("timestamp", "5 minutes")
        .groupBy(window("timestamp", "1 minutes"))
        .avg()
    )

    sensor = (
        sensor.withColumn("startdate", sensor["window"]["start"])
        .withColumn("window", sensor["window"]["end"])
        .withColumnRenamed("window", "enddate")
        .withColumnRenamed("avg(value)", "value")
        .withColumnRenamed("avg(voltage)", "voltage")
        .withColumnRenamed("avg(density)", "density")
    )

    sensor.writeStream.foreachBatch(write_jdbc).start().awaitTermination()

# Backup code
# ###########
# sensor.printSchema()
# def process_row(row):
#     print(f'{row["startdate"]} -> {row["enddate"]} = {row["value"]}')

# sensor.writeStream.foreach(process_row).start().awaitTermination()
# sensor.writeStream.format("console").outputMode("append").start().awaitTermination()
