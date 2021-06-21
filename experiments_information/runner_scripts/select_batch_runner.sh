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
  
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_pipeline-1_percent_Ad_click_on_taobao_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LB|1"
  INSTRUMENTED_SHEET_NAME="I|LB|1"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_pipeline-3_percent_Ad_click_on_taobao_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LB|3"
  INSTRUMENTED_SHEET_NAME="I|LB|3"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_pipeline-5_percent_Ad_click_on_taobao_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LB|5"
  INSTRUMENTED_SHEET_NAME="I|LB|5"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_pipeline-10_percent_Ad_click_on_taobao_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LB|10"
  INSTRUMENTED_SHEET_NAME="I|LB|10"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_pipeline-20_percent_Ad_click_on_taobao_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LB|20"
  INSTRUMENTED_SHEET_NAME="I|LB|20"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_pipeline-30_percent_Ad_click_on_taobao_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LB|30"
  INSTRUMENTED_SHEET_NAME="I|LB|30"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_pipeline-40_percent_Ad_click_on_taobao_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LB|40"
  INSTRUMENTED_SHEET_NAME="I|LB|40"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_pipeline-50_percent_Ad_click_on_taobao_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LB|50"
  INSTRUMENTED_SHEET_NAME="I|LB|50"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_pipeline-60_percent_Ad_click_on_taobao_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LB|60"
  INSTRUMENTED_SHEET_NAME="I|LB|60"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_pipeline-70_percent_Ad_click_on_taobao_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LB|70"
  INSTRUMENTED_SHEET_NAME="I|LB|70"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_pipeline-80_percent_Ad_click_on_taobao_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LB|80"
  INSTRUMENTED_SHEET_NAME="I|LB|80"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_pipeline-90_percent_Ad_click_on_taobao_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LB|90"
  INSTRUMENTED_SHEET_NAME="I|LB|90"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_pipeline-100_percent_Ad_click_on_taobao_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LB|100"
  INSTRUMENTED_SHEET_NAME="I|LB|100"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_pipeline-1_percent_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|1"
  INSTRUMENTED_SHEET_NAME="I|M|1"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_pipeline-3_percent_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|3"
  INSTRUMENTED_SHEET_NAME="I|M|3"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_pipeline-5_percent_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|5"
  INSTRUMENTED_SHEET_NAME="I|M|5"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_pipeline-10_percent_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|10"
  INSTRUMENTED_SHEET_NAME="I|M|10"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_pipeline-20_percent_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|20"
  INSTRUMENTED_SHEET_NAME="I|M|20"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_pipeline-30_percent_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|30"
  INSTRUMENTED_SHEET_NAME="I|M|30"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_pipeline-40_percent_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|40"
  INSTRUMENTED_SHEET_NAME="I|M|40"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_pipeline-50_percent_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|50"
  INSTRUMENTED_SHEET_NAME="I|M|50"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_pipeline-60_percent_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|60"
  INSTRUMENTED_SHEET_NAME="I|M|60"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_pipeline-70_percent_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|70"
  INSTRUMENTED_SHEET_NAME="I|M|70"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_pipeline-80_percent_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|80"
  INSTRUMENTED_SHEET_NAME="I|M|80"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_pipeline-90_percent_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|90"
  INSTRUMENTED_SHEET_NAME="I|M|90"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_pipeline-100_percent_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|100"
  INSTRUMENTED_SHEET_NAME="I|M|100"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_pipeline-1_percent_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|1"
  INSTRUMENTED_SHEET_NAME="I|B|1"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_pipeline-3_percent_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|3"
  INSTRUMENTED_SHEET_NAME="I|B|3"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_pipeline-5_percent_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|5"
  INSTRUMENTED_SHEET_NAME="I|B|5"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_pipeline-10_percent_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|10"
  INSTRUMENTED_SHEET_NAME="I|B|10"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_pipeline-20_percent_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|20"
  INSTRUMENTED_SHEET_NAME="I|B|20"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_pipeline-30_percent_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|30"
  INSTRUMENTED_SHEET_NAME="I|B|30"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_pipeline-40_percent_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|40"
  INSTRUMENTED_SHEET_NAME="I|B|40"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_pipeline-50_percent_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|50"
  INSTRUMENTED_SHEET_NAME="I|B|50"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_pipeline-60_percent_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|60"
  INSTRUMENTED_SHEET_NAME="I|B|60"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_pipeline-70_percent_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|70"
  INSTRUMENTED_SHEET_NAME="I|B|70"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_pipeline-80_percent_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|80"
  INSTRUMENTED_SHEET_NAME="I|B|80"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_pipeline-90_percent_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|90"
  INSTRUMENTED_SHEET_NAME="I|B|90"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_pipeline-100_percent_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|100"
  INSTRUMENTED_SHEET_NAME="I|B|100"
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