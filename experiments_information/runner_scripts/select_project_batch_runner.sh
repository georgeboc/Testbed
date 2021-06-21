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
  
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-1_projected_columns_1_percent_sf_Ad_click_on_taobao_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LB|1|1c"
  INSTRUMENTED_SHEET_NAME="I|LB|1|1c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-2_projected_columns_1_percent_sf_Ad_click_on_taobao_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LB|1|2c"
  INSTRUMENTED_SHEET_NAME="I|LB|1|2c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-3_projected_columns_1_percent_sf_Ad_click_on_taobao_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LB|1|3c"
  INSTRUMENTED_SHEET_NAME="I|LB|1|3c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-4_projected_columns_1_percent_sf_Ad_click_on_taobao_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LB|1|4c"
  INSTRUMENTED_SHEET_NAME="I|LB|1|4c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-5_projected_columns_1_percent_sf_Ad_click_on_taobao_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LB|1|5c"
  INSTRUMENTED_SHEET_NAME="I|LB|1|5c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-6_projected_columns_1_percent_sf_Ad_click_on_taobao_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LB|1|6c"
  INSTRUMENTED_SHEET_NAME="I|LB|1|6c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-1_projected_columns_5_percent_sf_Ad_click_on_taobao_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LB|5|1c"
  INSTRUMENTED_SHEET_NAME="I|LB|5|1c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-2_projected_columns_5_percent_sf_Ad_click_on_taobao_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LB|5|2c"
  INSTRUMENTED_SHEET_NAME="I|LB|5|2c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-3_projected_columns_5_percent_sf_Ad_click_on_taobao_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LB|5|3c"
  INSTRUMENTED_SHEET_NAME="I|LB|5|3c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-4_projected_columns_5_percent_sf_Ad_click_on_taobao_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LB|5|4c"
  INSTRUMENTED_SHEET_NAME="I|LB|5|4c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-5_projected_columns_5_percent_sf_Ad_click_on_taobao_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LB|5|5c"
  INSTRUMENTED_SHEET_NAME="I|LB|5|5c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-6_projected_columns_5_percent_sf_Ad_click_on_taobao_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LB|5|6c"
  INSTRUMENTED_SHEET_NAME="I|LB|5|6c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-1_projected_columns_20_percent_sf_Ad_click_on_taobao_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LB|20|1c"
  INSTRUMENTED_SHEET_NAME="I|LB|20|1c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-2_projected_columns_20_percent_sf_Ad_click_on_taobao_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LB|20|2c"
  INSTRUMENTED_SHEET_NAME="I|LB|20|2c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-3_projected_columns_20_percent_sf_Ad_click_on_taobao_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LB|20|3c"
  INSTRUMENTED_SHEET_NAME="I|LB|20|3c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-4_projected_columns_20_percent_sf_Ad_click_on_taobao_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LB|20|4c"
  INSTRUMENTED_SHEET_NAME="I|LB|20|4c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-5_projected_columns_20_percent_sf_Ad_click_on_taobao_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LB|20|5c"
  INSTRUMENTED_SHEET_NAME="I|LB|20|5c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-6_projected_columns_20_percent_sf_Ad_click_on_taobao_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LB|20|6c"
  INSTRUMENTED_SHEET_NAME="I|LB|20|6c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-1_projected_columns_50_percent_sf_Ad_click_on_taobao_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LB|50|1c"
  INSTRUMENTED_SHEET_NAME="I|LB|50|1c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-2_projected_columns_50_percent_sf_Ad_click_on_taobao_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LB|50|2c"
  INSTRUMENTED_SHEET_NAME="I|LB|50|2c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-3_projected_columns_50_percent_sf_Ad_click_on_taobao_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LB|50|3c"
  INSTRUMENTED_SHEET_NAME="I|LB|50|3c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-4_projected_columns_50_percent_sf_Ad_click_on_taobao_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LB|50|4c"
  INSTRUMENTED_SHEET_NAME="I|LB|50|4c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-5_projected_columns_50_percent_sf_Ad_click_on_taobao_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LB|50|5c"
  INSTRUMENTED_SHEET_NAME="I|LB|50|5c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-6_projected_columns_50_percent_sf_Ad_click_on_taobao_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LB|50|6c"
  INSTRUMENTED_SHEET_NAME="I|LB|50|6c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-1_projected_columns_100_percent_sf_Ad_click_on_taobao_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LB|100|1c"
  INSTRUMENTED_SHEET_NAME="I|LB|100|1c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-2_projected_columns_100_percent_sf_Ad_click_on_taobao_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LB|100|2c"
  INSTRUMENTED_SHEET_NAME="I|LB|100|2c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-3_projected_columns_100_percent_sf_Ad_click_on_taobao_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LB|100|3c"
  INSTRUMENTED_SHEET_NAME="I|LB|100|3c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-4_projected_columns_100_percent_sf_Ad_click_on_taobao_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LB|100|4c"
  INSTRUMENTED_SHEET_NAME="I|LB|100|4c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-5_projected_columns_100_percent_sf_Ad_click_on_taobao_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LB|100|5c"
  INSTRUMENTED_SHEET_NAME="I|LB|100|5c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-6_projected_columns_100_percent_sf_Ad_click_on_taobao_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|LB|100|6c"
  INSTRUMENTED_SHEET_NAME="I|LB|100|6c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-1_projected_columns_1_percent_sf_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|1|1c"
  INSTRUMENTED_SHEET_NAME="I|M|1|1c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-2_projected_columns_1_percent_sf_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|1|2c"
  INSTRUMENTED_SHEET_NAME="I|M|1|2c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-3_projected_columns_1_percent_sf_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|1|3c"
  INSTRUMENTED_SHEET_NAME="I|M|1|3c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-5_projected_columns_1_percent_sf_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|1|5c"
  INSTRUMENTED_SHEET_NAME="I|M|1|5c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-10_projected_columns_1_percent_sf_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|1|10c"
  INSTRUMENTED_SHEET_NAME="I|M|1|10c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-15_projected_columns_1_percent_sf_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|1|15c"
  INSTRUMENTED_SHEET_NAME="I|M|1|15c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-20_projected_columns_1_percent_sf_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|1|20c"
  INSTRUMENTED_SHEET_NAME="I|M|1|20c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-25_projected_columns_1_percent_sf_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|1|25c"
  INSTRUMENTED_SHEET_NAME="I|M|1|25c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-28_projected_columns_1_percent_sf_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|1|28c"
  INSTRUMENTED_SHEET_NAME="I|M|1|28c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-1_projected_columns_5_percent_sf_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|5|1c"
  INSTRUMENTED_SHEET_NAME="I|M|5|1c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-2_projected_columns_5_percent_sf_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|5|2c"
  INSTRUMENTED_SHEET_NAME="I|M|5|2c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-3_projected_columns_5_percent_sf_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|5|3c"
  INSTRUMENTED_SHEET_NAME="I|M|5|3c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-5_projected_columns_5_percent_sf_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|5|5c"
  INSTRUMENTED_SHEET_NAME="I|M|5|5c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-10_projected_columns_5_percent_sf_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|5|10c"
  INSTRUMENTED_SHEET_NAME="I|M|5|10c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-15_projected_columns_5_percent_sf_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|5|15c"
  INSTRUMENTED_SHEET_NAME="I|M|5|15c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-20_projected_columns_5_percent_sf_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|5|20c"
  INSTRUMENTED_SHEET_NAME="I|M|5|20c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-25_projected_columns_5_percent_sf_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|5|25c"
  INSTRUMENTED_SHEET_NAME="I|M|5|25c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-28_projected_columns_5_percent_sf_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|5|28c"
  INSTRUMENTED_SHEET_NAME="I|M|5|28c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-1_projected_columns_20_percent_sf_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|20|1c"
  INSTRUMENTED_SHEET_NAME="I|M|20|1c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-2_projected_columns_20_percent_sf_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|20|2c"
  INSTRUMENTED_SHEET_NAME="I|M|20|2c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-3_projected_columns_20_percent_sf_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|20|3c"
  INSTRUMENTED_SHEET_NAME="I|M|20|3c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-5_projected_columns_20_percent_sf_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|20|5c"
  INSTRUMENTED_SHEET_NAME="I|M|20|5c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-10_projected_columns_20_percent_sf_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|20|10c"
  INSTRUMENTED_SHEET_NAME="I|M|20|10c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-15_projected_columns_20_percent_sf_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|20|15c"
  INSTRUMENTED_SHEET_NAME="I|M|20|15c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-20_projected_columns_20_percent_sf_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|20|20c"
  INSTRUMENTED_SHEET_NAME="I|M|20|20c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-25_projected_columns_20_percent_sf_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|20|25c"
  INSTRUMENTED_SHEET_NAME="I|M|20|25c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-28_projected_columns_20_percent_sf_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|20|28c"
  INSTRUMENTED_SHEET_NAME="I|M|20|28c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-1_projected_columns_50_percent_sf_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|50|1c"
  INSTRUMENTED_SHEET_NAME="I|M|50|1c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-2_projected_columns_50_percent_sf_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|50|2c"
  INSTRUMENTED_SHEET_NAME="I|M|50|2c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-3_projected_columns_50_percent_sf_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|50|3c"
  INSTRUMENTED_SHEET_NAME="I|M|50|3c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-5_projected_columns_50_percent_sf_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|50|5c"
  INSTRUMENTED_SHEET_NAME="I|M|50|5c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-10_projected_columns_50_percent_sf_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|50|10c"
  INSTRUMENTED_SHEET_NAME="I|M|50|10c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-15_projected_columns_50_percent_sf_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|50|15c"
  INSTRUMENTED_SHEET_NAME="I|M|50|15c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-20_projected_columns_50_percent_sf_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|50|20c"
  INSTRUMENTED_SHEET_NAME="I|M|50|20c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-25_projected_columns_50_percent_sf_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|50|25c"
  INSTRUMENTED_SHEET_NAME="I|M|50|25c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-28_projected_columns_50_percent_sf_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|50|28c"
  INSTRUMENTED_SHEET_NAME="I|M|50|28c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-1_projected_columns_100_percent_sf_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|100|1c"
  INSTRUMENTED_SHEET_NAME="I|M|100|1c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-2_projected_columns_100_percent_sf_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|100|2c"
  INSTRUMENTED_SHEET_NAME="I|M|100|2c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-3_projected_columns_100_percent_sf_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|100|3c"
  INSTRUMENTED_SHEET_NAME="I|M|100|3c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-5_projected_columns_100_percent_sf_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|100|5c"
  INSTRUMENTED_SHEET_NAME="I|M|100|5c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-10_projected_columns_100_percent_sf_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|100|10c"
  INSTRUMENTED_SHEET_NAME="I|M|100|10c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-15_projected_columns_100_percent_sf_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|100|15c"
  INSTRUMENTED_SHEET_NAME="I|M|100|15c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-20_projected_columns_100_percent_sf_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|100|20c"
  INSTRUMENTED_SHEET_NAME="I|M|100|20c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-25_projected_columns_100_percent_sf_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|100|25c"
  INSTRUMENTED_SHEET_NAME="I|M|100|25c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-28_projected_columns_100_percent_sf_Obama_visitor_logs_1g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|M|100|28c"
  INSTRUMENTED_SHEET_NAME="I|M|100|28c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-1_projected_columns_1_percent_sf_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|1|1c"
  INSTRUMENTED_SHEET_NAME="I|B|1|1c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-2_projected_columns_1_percent_sf_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|1|2c"
  INSTRUMENTED_SHEET_NAME="I|B|1|2c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-3_projected_columns_1_percent_sf_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|1|3c"
  INSTRUMENTED_SHEET_NAME="I|B|1|3c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-4_projected_columns_1_percent_sf_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|1|4c"
  INSTRUMENTED_SHEET_NAME="I|B|1|4c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-5_projected_columns_1_percent_sf_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|1|5c"
  INSTRUMENTED_SHEET_NAME="I|B|1|5c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-6_projected_columns_1_percent_sf_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|1|6c"
  INSTRUMENTED_SHEET_NAME="I|B|1|6c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-7_projected_columns_1_percent_sf_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|1|7c"
  INSTRUMENTED_SHEET_NAME="I|B|1|7c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-1_projected_columns_5_percent_sf_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|5|1c"
  INSTRUMENTED_SHEET_NAME="I|B|5|1c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-2_projected_columns_5_percent_sf_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|5|2c"
  INSTRUMENTED_SHEET_NAME="I|B|5|2c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-3_projected_columns_5_percent_sf_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|5|3c"
  INSTRUMENTED_SHEET_NAME="I|B|5|3c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-4_projected_columns_5_percent_sf_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|5|4c"
  INSTRUMENTED_SHEET_NAME="I|B|5|4c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-5_projected_columns_5_percent_sf_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|5|5c"
  INSTRUMENTED_SHEET_NAME="I|B|5|5c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-6_projected_columns_5_percent_sf_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|5|6c"
  INSTRUMENTED_SHEET_NAME="I|B|5|6c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-7_projected_columns_5_percent_sf_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|5|7c"
  INSTRUMENTED_SHEET_NAME="I|B|5|7c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-1_projected_columns_20_percent_sf_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|20|1c"
  INSTRUMENTED_SHEET_NAME="I|B|20|1c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-2_projected_columns_20_percent_sf_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|20|2c"
  INSTRUMENTED_SHEET_NAME="I|B|20|2c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-3_projected_columns_20_percent_sf_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|20|3c"
  INSTRUMENTED_SHEET_NAME="I|B|20|3c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-4_projected_columns_20_percent_sf_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|20|4c"
  INSTRUMENTED_SHEET_NAME="I|B|20|4c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-5_projected_columns_20_percent_sf_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|20|5c"
  INSTRUMENTED_SHEET_NAME="I|B|20|5c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-6_projected_columns_20_percent_sf_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|20|6c"
  INSTRUMENTED_SHEET_NAME="I|B|20|6c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-7_projected_columns_20_percent_sf_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|20|7c"
  INSTRUMENTED_SHEET_NAME="I|B|20|7c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-1_projected_columns_50_percent_sf_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|50|1c"
  INSTRUMENTED_SHEET_NAME="I|B|50|1c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-2_projected_columns_50_percent_sf_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|50|2c"
  INSTRUMENTED_SHEET_NAME="I|B|50|2c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-3_projected_columns_50_percent_sf_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|50|3c"
  INSTRUMENTED_SHEET_NAME="I|B|50|3c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-4_projected_columns_50_percent_sf_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|50|4c"
  INSTRUMENTED_SHEET_NAME="I|B|50|4c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-5_projected_columns_50_percent_sf_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|50|5c"
  INSTRUMENTED_SHEET_NAME="I|B|50|5c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-6_projected_columns_50_percent_sf_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|50|6c"
  INSTRUMENTED_SHEET_NAME="I|B|50|6c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-7_projected_columns_50_percent_sf_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|50|7c"
  INSTRUMENTED_SHEET_NAME="I|B|50|7c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-1_projected_columns_100_percent_sf_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|100|1c"
  INSTRUMENTED_SHEET_NAME="I|B|100|1c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-2_projected_columns_100_percent_sf_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|100|2c"
  INSTRUMENTED_SHEET_NAME="I|B|100|2c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-3_projected_columns_100_percent_sf_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|100|3c"
  INSTRUMENTED_SHEET_NAME="I|B|100|3c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-4_projected_columns_100_percent_sf_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|100|4c"
  INSTRUMENTED_SHEET_NAME="I|B|100|4c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-5_projected_columns_100_percent_sf_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|100|5c"
  INSTRUMENTED_SHEET_NAME="I|B|100|5c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-6_projected_columns_100_percent_sf_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|100|6c"
  INSTRUMENTED_SHEET_NAME="I|B|100|6c"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$TIMED_SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
    
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/select_project_pipeline-7_projected_columns_100_percent_sf_Thunderbird_30g.json"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/select_project_pipeline.xlsx"
  TIMED_SHEET_NAME="T|B|100|7c"
  INSTRUMENTED_SHEET_NAME="I|B|100|7c"
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