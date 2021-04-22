#! /bin/bash

export JAR_PATH=target/Testbed-1.0-SNAPSHOT-jar-with-dependencies.jar
export SCRIPTS_PATH=scripts

export PIPELINE=pipelines/pipeline.json
export OUTPUT=output/operation_instrumentations.xlsx
export SHEET_NAME=Test
export TOLERABLE_ERROR_PERCENTAGE=5

function execute_experiments {
    install_last_version
    for i in {1..3}
    do
        clear_caches
        execute_timed_experiment_with_MapReduce
        clear_caches
        execute_timed_experiment_with_Spark
    done
    execute_instrumented_experiment
}

function install_last_version {
  echo "Installing Testbed's last version"
  sh $SCRIPTS_PATH/install.sh
}

function clear_caches () {
  echo "Clearing JVM Cache"
  sysctl -w vm.drop_caches=3 > /dev/null
  sync && echo 3 | sudo tee /proc/sys/vm/drop_caches > /dev/null
}

function execute_timed_experiment_with_MapReduce () {
    echo "Executing MapReduce timed experiment #$i"
    timeout 10m java -jar $JAR_PATH \
    --tolerable-error-percentage $TOLERABLE_ERROR_PERCENTAGE \
    --framework-configuration TimedMapReduce \
    --pipeline $PIPELINE \
    --output $OUTPUT \
    --sheet-name $SHEET_NAME
}

function execute_timed_experiment_with_Spark () {
    echo "Executing Spark timed experiment #$i"
    timeout 10m java -jar $JAR_PATH \
    --tolerable-error-percentage $TOLERABLE_ERROR_PERCENTAGE \
    --framework-configuration TimedSpark \
    --pipeline $PIPELINE \
    --output $OUTPUT \
    --sheet-name $SHEET_NAME
}

function execute_instrumented_experiment () {
    echo "Executing instrumented experiment #$i"
    timeout 10m java -jar $JAR_PATH \
    --tolerable-error-percentage $TOLERABLE_ERROR_PERCENTAGE \
    --framework-configuration InstrumentedSpark \
    --pipeline $PIPELINE \
    --output $OUTPUT \
    --sheet-name "$SHEET_NAME Instrumentation"
}

execute_experiments