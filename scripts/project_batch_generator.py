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

PIPELINE_TEMPLATE = f"{SCRIPTS_TEMPLATES}/project_pipeline.json.template"
BATCH_RUNNER_TEMPLATE = f"{SCRIPTS_TEMPLATES}/batch_runner.sh.template"

OUTPUT_FILENAME_FORMAT = "project_pipeline-{{selectivity_factor_percentage}}_percent_{{dataset_name}}.json"
OUTPUT_BATCH_RUNNER_FILENAME = f"{SCRIPTS}/project_batch_runner.sh"

SELECTIVITY_FACTOR_PERCENTAGES = [15.5, 31, 46, 62, 77, 93, 100]

BATCH_ENTRY = """  
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/{{pipeline_filename}}"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/{{output_filename}}"
  SHEET_NAME="{{sheet_name}}"
  INSTRUMENTED_SHEET_NAME="{{instrumented_sheet_name}}"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '9'"
  
"""

def main():
    dataset_names = get_dataset_names()
    for dataset_name in dataset_names:
        create_pipelines(dataset_name)
    create_bash_runner(dataset_names)

def create_pipelines(dataset_name):
    for selectivity_factor_percentage in SELECTIVITY_FACTOR_PERCENTAGES:
        pipeline_content = Template(read_file_contents(PIPELINE_TEMPLATE)).render(dataset_name=dataset_name,
                                                                 selectivity_factor=get_normalized_selectivity_factor(
                                                                         selectivity_factor_percentage))
        print("Pipeline content:", pipeline_content)
        filename = Template(OUTPUT_FILENAME_FORMAT).render(dataset_name=dataset_name,
                                                           selectivity_factor_percentage=selectivity_factor_percentage)
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
                                                                           selectivity_factor_percentage=selectivity_factor_percentage)
                                   for selectivity_factor_percentage in SELECTIVITY_FACTOR_PERCENTAGES])
    return pipeline_filenames

def get_output_filename(pipeline_filename):
    return f"{pipeline_filename.split('-')[0]}.xlsx"

def get_sheet_name(pipeline_filename):
    unparsed_sheet_name = pipeline_filename.split('-')[1]
    pattern = r'(.*)_percent_(.*).json'
    match = re.search(pattern, unparsed_sheet_name)
    percentage, dataset_name = match.groups()
    return f"{DATASETS_MAPPING[dataset_name]} | {percentage}% SF" # Column Selection Factor

if __name__ == "__main__":
    main()
