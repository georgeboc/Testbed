#!/bin/bash
# Execute from Testbed root directory

export DISABLE_INSTALL_LAST_VERSION=$2

export SCRIPTS_PATH=scripts
export EXPERIMENTS_RUNNER_SCRIPT_PATH="/bin/bash $SCRIPTS_PATH/experiments_runner.sh"

function install_last_version {
  echo "Installing Testbed's last version"
  git pull
  sh $SCRIPTS_PATH/install.sh
}

function execute_batch () {
  
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/join_pipeline-Ad_click_on_taobao_1g-AdGroupId-AdGroupId-Ad_click_on_taobao_Ad_feature.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/join_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LBjLA"
  INSTRUMENTED_SHEET_NAME="I|LBjLA"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/join_pipeline-Ad_click_on_taobao_1g-User-UserId-Ad_click_on_taobao_User_profile.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/join_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LBjLU"
  INSTRUMENTED_SHEET_NAME="I|LBjLU"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
  
}

function main () {
  if [ "$DISABLE_INSTALL_LAST_VERSION" != "--disable-install-last-version" ]
  then
    install_last_version
  fi
  execute_batch
}

if [ "$1" == "!" ]
then
  main
else
  screen bash "$0" ! "$@"
fi