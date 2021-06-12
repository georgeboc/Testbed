from pyspark.sql import SparkSession
from pyspark.sql.functions import col

MASTER = "master"
LOCAL = "local[*]"
LEFT_PREFIX = "Left"
RIGHT_PREFIX = "Right"

LEFT_DATASET_PATH = "input/Ad_click_on_taobao_10000/parsed_dataset"
RIGHT_DATASET_PATH = "input/Ad_click_on_taobao_V2_10000/parsed_dataset"
PROJECTED_COLUMNS = ["AdGroupId", "Clk", "DateTime", "NonClk"]
SELECT_QUERY = "User <= '233060'"
LEFT_JOIN_ATTRIBUTE = "LeftDateTime"
RIGHT_JOIN_ATTRIBUTE = "RightDateTime"
OUTPUT_DATASET_PATH = "output"

def add_prefix(dataframe, prefix):
    renamed_dataframe = dataframe
    for column_name in dataframe.columns:
        renamed_dataframe = renamed_dataframe.withColumnRenamed(column_name, prefix + column_name)
    return renamed_dataframe

if __name__ == "__main__":
    spark = SparkSession \
        .builder \
        .config(MASTER, LOCAL) \
        .getOrCreate()

    left_dataset = spark.read.parquet(LEFT_DATASET_PATH)
    right_dataset = spark.read.parquet(RIGHT_DATASET_PATH)
    projectedDataset = left_dataset.select(*PROJECTED_COLUMNS)
    projectedDatasetWithPrefix = add_prefix(projectedDataset, LEFT_PREFIX)
    selectedDataset = right_dataset.filter(SELECT_QUERY)
    selectedDatasetWithPrefix = add_prefix(selectedDataset, RIGHT_PREFIX)
    joinedDataset = projectedDatasetWithPrefix.join(selectedDatasetWithPrefix, col(LEFT_JOIN_ATTRIBUTE) == col(RIGHT_JOIN_ATTRIBUTE))
    joinedDataset.write.parquet(OUTPUT_DATASET_PATH)
    joinedDataset.explain()