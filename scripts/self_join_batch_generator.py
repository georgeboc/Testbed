#/bin/python3
# Execute from Testbed root directory

import re
from jinja2 import Template

SCRIPTS = "scripts"
SCRIPTS_TEMPLATES = f"{SCRIPTS}/templates"
SCRIPTS = "scripts"
PIPELINES = "pipelines"

DATASETS_MAPPING = {
    "Ad_click_on_taobao_512m": "little (512M)",
    "Ad_click_on_taobao_1g": "little (1G)",
    "Obama_visitor_logs_1g": "medium (1G)",
    "Thunderbird_30g": "big (30G)"
}

PIPELINE_TEMPLATE = f"{SCRIPTS_TEMPLATES}/self_join_pipeline.json.template"
BATCH_RUNNER_TEMPLATE = f"{SCRIPTS_TEMPLATES}/batch_runner.sh.template"

OUTPUT_FILENAME_FORMAT = "self_join_pipeline-column_number_{{column_index}}_{{dataset_name}}.json"
OUTPUT_BATCH_RUNNER_FILENAME = f"{SCRIPTS}/self_join_batch_runner.sh"

DATASETS_COLUMN_NAMES_MAPPING = {
    "Ad_click_on_taobao_512m": ["User", "DateTime", "AdGroupId", "PID", "NonClk", "Clk"],
    "Ad_click_on_taobao_1g": ["User", "DateTime", "AdGroupId", "PID", "NonClk", "Clk"],
    "Obama_visitor_logs_1g": ["NAMELAST", "NAMEFIRST", "NAMEMID", "UIN", "BDGNBR", "TYPE_OF_ACCESS", "TOA", "POA", "TOD", "POD", "APPT_MADE_DATE", "APPT_START_DATE", "APPT_END_DATE", "APPT_CANCEL_DATE", "Total_People", "LAST_UPDATEDBY", "POST", "LastEntryDate", "TERMINAL_SUFFIX", "visitee_namelast", "visitee_namefirst", "MEETING_LOC", "MEETING_ROOM", "CALLER_NAME_LAST", "CALLER_NAME_FIRST", "CALLER_ROOM", "Description", "RELEASE_DATE"],
    "Thunderbird_30g": ["IsAlertMessage?", "Timestamp", "Date", "Node", "DateTime", "URI", "Content"]
}

BATCH_ENTRY = """  
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/{{pipeline_filename}}"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/{{output_filename}}"
  SHEET_NAME="{{sheet_name}}"
  INSTRUMENTED_SHEET_NAME="{{instrumented_sheet_name}}"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
  
"""

def main():
    dataset_names = get_dataset_names()
    for dataset_name in dataset_names:
        create_pipelines(dataset_name)
    create_bash_runner(dataset_names)

def create_pipelines(dataset_name):
    for column_index, column_name in enumerate(DATASETS_COLUMN_NAMES_MAPPING[dataset_name]):
        pipeline_content = Template(read_file_contents(PIPELINE_TEMPLATE)).render(dataset_name=dataset_name,
                                                                                  column_name=column_name)
        print("Pipeline content:", pipeline_content)
        filename = Template(OUTPUT_FILENAME_FORMAT).render(dataset_name=dataset_name,
                                                           column_index=column_index)
        print("Filename generated:", filename)
        write_file_contents(filename, pipeline_content)

def get_dataset_names():
    return list(DATASETS_MAPPING.keys())

def read_file_contents(filename):
    with open(filename, 'r') as file:
        return file.read()

def get_normalized_selectivity_factor(selectivity_factor_percentage):
    return selectivity_factor_percentage/100.0

def write_file_contents(filename, contents):
    with open(filename, 'w') as file:
        return file.write(contents)

def create_bash_runner(dataset_names):
    pipeline_filenames = get_pipeline_filenames(dataset_names)
    batches = ""
    for pipeline_filename in pipeline_filenames:
        output_filename = get_output_filename(pipeline_filename)
        sheet_name = get_sheet_name(pipeline_filename)
        batch_entry = Template(BATCH_ENTRY).render(pipeline_filename=pipeline_filename,
                                                   output_filename=output_filename,
                                                   sheet_name=sheet_name,
                                                   instrumented_sheet_name=get_instrumented_sheet_name(sheet_name))
        batches += batch_entry
    batch_runner_content = Template(read_file_contents(BATCH_RUNNER_TEMPLATE)).render(batches=batches)
    write_file_contents(OUTPUT_BATCH_RUNNER_FILENAME, batch_runner_content)

def get_instrumented_sheet_name(sheet_name):
    return f"{sheet_name} Ins."

def get_pipeline_filenames(dataset_names):
    pipeline_filenames = []
    for dataset_name in dataset_names:
        pipeline_filenames.extend([Template(OUTPUT_FILENAME_FORMAT).render(dataset_name=dataset_name,
                                                                           column_index=column_index)
                                   for column_index in range(len(DATASETS_COLUMN_NAMES_MAPPING[dataset_name]))])
    return pipeline_filenames

def get_output_filename(pipeline_filename):
    return f"{pipeline_filename.split('-')[0]}.xlsx"

def get_sheet_name(pipeline_filename):
    unparsed_sheet_name = pipeline_filename.split('-')[1]
    pattern = r'column_number_(\d+)_(.*).json'
    match = re.search(pattern, unparsed_sheet_name)
    column_index, dataset_name = match.groups()
    return f"{DATASETS_MAPPING[dataset_name]} | col. #{int(column_index) + 1}"

if __name__ == "__main__":
    main()
