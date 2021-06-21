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
  
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/project_pipeline-1_projected_columns_Ad_click_on_taobao_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LB|1c"
  INSTRUMENTED_SHEET_NAME="I|LB|1c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/project_pipeline-2_projected_columns_Ad_click_on_taobao_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LB|2c"
  INSTRUMENTED_SHEET_NAME="I|LB|2c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/project_pipeline-3_projected_columns_Ad_click_on_taobao_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LB|3c"
  INSTRUMENTED_SHEET_NAME="I|LB|3c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/project_pipeline-4_projected_columns_Ad_click_on_taobao_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LB|4c"
  INSTRUMENTED_SHEET_NAME="I|LB|4c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/project_pipeline-5_projected_columns_Ad_click_on_taobao_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LB|5c"
  INSTRUMENTED_SHEET_NAME="I|LB|5c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/project_pipeline-6_projected_columns_Ad_click_on_taobao_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LB|6c"
  INSTRUMENTED_SHEET_NAME="I|LB|6c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/project_pipeline-1_projected_columns_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|1c"
  INSTRUMENTED_SHEET_NAME="I|M|1c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/project_pipeline-2_projected_columns_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|2c"
  INSTRUMENTED_SHEET_NAME="I|M|2c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/project_pipeline-3_projected_columns_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|3c"
  INSTRUMENTED_SHEET_NAME="I|M|3c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/project_pipeline-5_projected_columns_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|5c"
  INSTRUMENTED_SHEET_NAME="I|M|5c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/project_pipeline-10_projected_columns_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|10c"
  INSTRUMENTED_SHEET_NAME="I|M|10c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/project_pipeline-15_projected_columns_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|15c"
  INSTRUMENTED_SHEET_NAME="I|M|15c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/project_pipeline-20_projected_columns_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|20c"
  INSTRUMENTED_SHEET_NAME="I|M|20c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/project_pipeline-25_projected_columns_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|25c"
  INSTRUMENTED_SHEET_NAME="I|M|25c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/project_pipeline-28_projected_columns_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|28c"
  INSTRUMENTED_SHEET_NAME="I|M|28c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/project_pipeline-1_projected_columns_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|1c"
  INSTRUMENTED_SHEET_NAME="I|B|1c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/project_pipeline-2_projected_columns_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|2c"
  INSTRUMENTED_SHEET_NAME="I|B|2c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/project_pipeline-3_projected_columns_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|3c"
  INSTRUMENTED_SHEET_NAME="I|B|3c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/project_pipeline-4_projected_columns_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|4c"
  INSTRUMENTED_SHEET_NAME="I|B|4c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/project_pipeline-5_projected_columns_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|5c"
  INSTRUMENTED_SHEET_NAME="I|B|5c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/project_pipeline-6_projected_columns_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|6c"
  INSTRUMENTED_SHEET_NAME="I|B|6c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/project_pipeline-7_projected_columns_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|7c"
  INSTRUMENTED_SHEET_NAME="I|B|7c"
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