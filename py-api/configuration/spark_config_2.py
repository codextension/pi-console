from pyspark.sql import SparkSession
from pyspark.sql.functions import explode
from pyspark.sql.functions import split

if __name__ == "__main__":
    spark = SparkSession.builder.appName("StructuredNetworkWordCount").getOrCreate()
    df = spark.readStream.format('kafka').option("kafka.bootstrap.servers", "localhost:9092").option("subscribe", "temperature").load()
    df.selectExpr("CAST(key AS STRING) AS key", "to_json(struct(*)) as value")
    df.printSchema()

    df.writeStream.format("console").option("truncate","false").start().awaitTermination()
