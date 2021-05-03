#! /bin/bash
# Execute from Testbed root directory

export JAR_PATH=target/Testbed-1.0-SNAPSHOT.jar
export SCRIPTS_PATH=scripts

export PIPELINE=hdfs://dtim:27000/user/bochileanu/pipelines/pipeline.json
export OUTPUT=hdfs://dtim:27000/user/bochileanu/output/operation_instrumentations.xlsx
export SHEET_NAME=Test
export INSTRUMENTED_SHEET_NAME="$SHEET_NAME Instrumentation"
export TOLERABLE_ERROR_PERCENTAGE=5

export GOOGLE_DRIVE_ACCOUNT=gdrive
export GOOGLE_DRIVE_PATH=Testbed/output

function execute_experiments {
    output_cleanup
    install_last_version
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

function output_cleanup () {
  echo "Ensuring output is a new file"
  hdfs dfs -rm $OUTPUT
}

function install_last_version {
  echo "Installing Testbed's last version"
  git pull
  sh $SCRIPTS_PATH/install.sh
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

if [ "$1" == "!" ]
then
  execute_experiments
else
  screen bash "$0" !
fi
