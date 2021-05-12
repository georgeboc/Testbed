#! /bin/bash
# Execute from Testbed root directory

export DISABLE_INSTALL_LAST_VERSION=$1

export SCRIPTS_PATH=scripts
export EXPERIMENTS_RUNNER_SCRIPT_PATH="/bin/bash $SCRIPTS_PATH/experiments_runner.sh"

function install_last_version {
  echo "Installing Testbed's last version"
  git pull
  sh $SCRIPTS_PATH/install.sh
}

function execute_batch () {
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_pipeline-90_percent_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/output/select_pipeline.xlsx"
  SHEET_NAME="Ad_click_on_taobao_512m | 80% Selectivity Factor"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH \"$PIPELINE\" \"$OUTPUT\" \"$SHEET_NAME\""
}

if [ -z $DISABLE_INSTALL_LAST_VERSION ] 
then
  install_last_version
fi
execute_batch
