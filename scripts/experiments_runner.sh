#! /bin/bash
# Execute from Testbed root directory

export JAR_PATH=target/Testbed-1.0-SNAPSHOT.jar
export SCRIPTS_PATH=scripts

export PIPELINE="$1"
export OUTPUT="$2"
export SHEET_NAME="$3"
export INSTRUMENTED_SHEET_NAME="$4"
export TOLERABLE_ERROR_PERCENTAGE="$5"

export GOOGLE_DRIVE_ACCOUNT=gdrive
export GOOGLE_DRIVE_PATH=Testbed/analysis_results

export OVERWRITE_SHEET=true

export TIMED_EXECUTION_ARGS=(
  --tolerable-error-percentage "$TOLERABLE_ERROR_PERCENTAGE"
  --output "$OUTPUT"
  --pipeline "$PIPELINE"
  --framework-name MapReduce
  --sheet-name "$SHEET_NAME"
)

export INSTRUMENTED_EXECUTION_ARGS=(
  --tolerable-error-percentage "$TOLERABLE_ERROR_PERCENTAGE"
  --output "$OUTPUT"
  --pipeline "$PIPELINE"
  --framework-name MapReduce
  --sheet-name "$SHEET_NAME"
  --instrumented
)

export SPARK_SUBMIT_ARGS=(
  --master yarn
  --deploy-mode cluster
  --conf spark.driver.memory="11301M"
  --conf spark.driver.memoryOverhead="851M"
  --conf spark.executor.memory="11301M"
  --conf spark.executor.memoryOverhead="851M"
  --conf spark.driver.cores="8"
  --conf spark.executor.cores="8"
  --conf spark.executor.instances="5"
)

export HADOOP="hadoop jar"
export SPARK="spark-submit ${SPARK_SUBMIT_ARGS[*]}"


echo "Parameters read: $PIPELINE, $OUTPUT, $SHEET_NAME, $INSTRUMENTED_SHEET_NAME"

function execute_experiments () {
  clear_output_file_caches
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

function clear_output_file_caches () {
  hdfs debug recoverLease -path "$OUTPUT" -retries 3
}

function clear_caches () {
  echo "Clearing JVM Cache"
  sudo sysctl -w vm.drop_caches=3 > /dev/null
  sync && echo 3 | sudo tee /proc/sys/vm/drop_caches > /dev/null
}

function execute_timed_experiment_with_MapReduce () {
  echo "Executing MapReduce timed experiment #$i"
  ${HADOOP} $JAR_PATH "${TIMED_EXECUTION_ARGS[@]}" ${OVERWRITE_SHEET:+--overwrite-sheet}
  OVERWRITE_SHEET=false
}

function execute_timed_experiment_with_Spark () {
  echo "Executing Spark timed experiment #$i"
  ${SPARK} $JAR_PATH "${TIMED_EXECUTION_ARGS[@]}" ${OVERWRITE_SHEET:+--overwrite-sheet}
  OVERWRITE_SHEET=false
}

function execute_instrumented_experiment () {
  echo "Executing instrumented experiment"
  ${SPARK} "${INSTRUMENTED_SHEET_NAME[@]}" ${OVERWRITE_SHEET:+--overwrite-sheet}
  OVERWRITE_SHEET=false
}

function upload_results_to_google_drive () {
  echo "Uploading results to google drive"
  hdfs dfs -copyToLocal "$OUTPUT" .
  OUTPUT_BASENAME=$(basename "$OUTPUT")
  rclone copy "$OUTPUT_BASENAME" $GOOGLE_DRIVE_ACCOUNT:$GOOGLE_DRIVE_PATH
  rm "$OUTPUT_BASENAME"
}

execute_experiments
