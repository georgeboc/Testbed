#! /bin/bash
# Execute from Testbed root directory

export JAR_PATH=target/Testbed-1.0-SNAPSHOT.jar
export SCRIPTS_PATH=scripts

export PIPELINE=$1
export OUTPUT=$2
export SHEET_NAME=$3
export INSTRUMENTED_SHEET_NAME="$SHEET_NAME Pipeline Instrumentation"
export TOLERABLE_ERROR_PERCENTAGE=5

export GOOGLE_DRIVE_ACCOUNT=gdrive
export GOOGLE_DRIVE_PATH=Testbed/analysis_results

echo "Parameters read: $PIPELINE, $OUTPUT, $SHEET_NAME"

function execute_experiments () {
    for i in {1..3}
    do
        clear_caches
        execute_timed_experiment_with_MapReduce
        clear_caches
        execute_timed_experiment_with_Spark
    done
    execute_instrumented_experiment
    upload_results_to_google_drive
}

function clear_caches () {
  echo "Clearing JVM Cache"
  sudo sysctl -w vm.drop_caches=3 > /dev/null
  sync && echo 3 | sudo tee /proc/sys/vm/drop_caches > /dev/null
}

function execute_timed_experiment_with_MapReduce () {
    echo "Executing MapReduce timed experiment #$i"
    timeout 10m hadoop jar $JAR_PATH \
    --tolerable-error-percentage $TOLERABLE_ERROR_PERCENTAGE \
    --output $OUTPUT \
    --pipeline $PIPELINE \
    --framework-name MapReduce \
    --sheet-name $SHEET_NAME
}

function execute_timed_experiment_with_Spark () {
    echo "Executing Spark timed experiment #$i"
    timeout 10m spark-submit \
    --master yarn \
    --deploy-mode cluster \
    --conf spark.driver.memory="11301M" \
    --conf spark.driver.memoryOverhead="851M" \
    --conf spark.executor.memory="11301M" \
    --conf spark.executor.memoryOverhead="851M" \
    --conf spark.driver.cores="8" \
    --conf spark.executor.cores="8" \
    --conf spark.executor.instances="5" \
    $JAR_PATH \
    --tolerable-error-percentage $TOLERABLE_ERROR_PERCENTAGE \
    --framework-name Spark \
    --pipeline $PIPELINE \
    --output $OUTPUT \
    --sheet-name $SHEET_NAME
}

function execute_instrumented_experiment () {
    echo "Executing instrumented experiment #$i"
    timeout 10m spark-submit \
    --master yarn \
    --deploy-mode cluster \
    --conf spark.driver.memory="11301M" \
    --conf spark.driver.memoryOverhead="851M" \
    --conf spark.executor.memory="11301M" \
    --conf spark.executor.memoryOverhead="851M" \
    --conf spark.driver.cores="8" \
    --conf spark.executor.cores="8" \
    --conf spark.executor.instances="5" \
    $JAR_PATH \
    --tolerable-error-percentage $TOLERABLE_ERROR_PERCENTAGE \
    --framework-name Spark \
    --instrumented \
    --pipeline $PIPELINE \
    --output $OUTPUT \
    --sheet-name "$INSTRUMENTED_SHEET_NAME"
}

function upload_results_to_google_drive () {
    echo "Uploading results to google drive"
    hdfs dfs -copyToLocal $OUTPUT .
    OUTPUT_BASENAME=$(basename $OUTPUT)
    rclone copy "$OUTPUT_BASENAME" $GOOGLE_DRIVE_ACCOUNT:$GOOGLE_DRIVE_PATH
    rm "$OUTPUT_BASENAME"
}

execute_experiments
