import findspark
import os
import threading
from pyspark.sql import SparkSession
from pyspark.sql.functions import explode
from pyspark.sql.functions import split
from pyspark.sql.types import *
from pyspark.sql.functions import *
from dust_config import DustConfig
from temperature_config import TemperatureConfig

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
] = f"--master local[2] --packages {','.join(packages)}  pyspark-shell"
os.environ["SPARK_HOME"] = "/home/pi/workspace/spark-2.4.4-bin-hadoop2.7"


class SparkInit:
    def __init__(self):
        findspark.init()
        findspark.find()

        self.spark = (
            SparkSession.builder.config("spark.streaming.concurrentJobs", "3")
            .config("spark.scheduler.mode", "FAIR")
            .appName("smarthome")
            .getOrCreate()
        )

        self.df = (
            self.spark.readStream.format("kafka")
            .option("kafka.bootstrap.servers", "192.168.178.63:9092")
            .option("subscribe", "dust,temperature")
            .option("startingOffsets", "latest")  # latest/earliest
            .load()
        )
        self.df = self.df.withColumn("value", self.df.value.astype("string"))
        self.dust_config = DustConfig(self.spark, self.df)
        self.temperature_config = TemperatureConfig(self.spark, self.df)

    def start(self):
        self.temperature_config.start_working()
        self.dust_config.start_working()

        self.spark.streams.awaitAnyTermination()


spark_init = SparkInit()
spark_init.start()
