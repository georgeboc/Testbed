#! /bin/bash
# Execute from Testbed root directory

export SCRIPTS_PATH=scripts
export EXPERIMENTS_RUNNER_SCRIPT_PATH="/bin/bash $SCRIPTS_PATH/experiments_runner.sh"

function install_last_version {
  echo "Installing Testbed's last version"
  git pull
  sh $SCRIPTS_PATH/install.sh
}

function execute_batch () {
  PIPELINE=hdfs://dtim:27000/user/bochileanu/pipelines/pipeline.json
  OUTPUT=hdfs://dtim:27000/user/bochileanu/output/operation_instrumentations.xlsx
  SHEET_NAME=New
  $($EXPERIMENTS_RUNNER_SCRIPT_PATH) $PIPELINE $OUTPUT $SHEET_NAME
}

install_last_version
execute_batch
