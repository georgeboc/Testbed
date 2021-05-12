#! /bin/bash
# Execute from Testbed root directory

export EXPERIMENTS_RUNNER_SCRIPT_PATH="/bin/bash experiments_runner.sh"

PIPELINE=hdfs://dtim:27000/user/bochileanu/pipelines/pipeline.json \
OUTPUT=hdfs://dtim:27000/user/bochileanu/output/operation_instrumentations.xlsx \
SHEET_NAME=New \
INSTRUMENTED_SHEET_NAME="New Instrumented" \
$($EXPERIMENTS_RUNNER_SCRIPT_PATH)