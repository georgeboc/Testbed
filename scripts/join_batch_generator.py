#/bin/python3
# Execute from Testbed root directory

import re
from jinja2 import Template

SCRIPTS = "scripts"
SCRIPTS_TEMPLATES = f"{SCRIPTS}/templates"
SCRIPTS = "scripts"
PIPELINES = "pipelines"

PIPELINE_TEMPLATE = f"{SCRIPTS_TEMPLATES}/join_pipeline.json.template"
BATCH_RUNNER_TEMPLATE = f"{SCRIPTS_TEMPLATES}/batch_runner.sh.template"

OUTPUT_FILENAME_FORMAT = "join_pipeline-{{left_dataset_name}}-{{left_column_name}}-{{right_column_name}}-{{right_dataset_name}}.json"
OUTPUT_BATCH_RUNNER_FILENAME = f"{SCRIPTS}/join_batch_runner.sh"

LEFT_DATASET_NAMES = ["Ad_click_on_taobao_512m", "Ad_click_on_taobao_1g"]
RIGHT_DATASET_NAMES = ["Ad_click_on_taobao_Ad_feature", "Ad_click_on_taobao_User_profile"]
RELATIONS = {
    "Ad_click_on_taobao_Ad_feature": ("AdGroupId", "AdGroupId"),
    "Ad_click_on_taobao_User_profile": ("User", "UserId")
}

DATASETS_MAPPING = {
    "Ad_click_on_taobao_512m": "little (512M)",
    "Ad_click_on_taobao_1g": "little (1G)",
    "Ad_click_on_taobao_Ad_feature": "AdFeature",
    "Ad_click_on_taobao_User_profile": "UserProfile"
}

LEFT = 0
RIGHT = 1

BATCH_ENTRY = """  
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/{{pipeline_filename}}"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/{{output_filename}}"
  SHEET_NAME="{{sheet_name}}"
  INSTRUMENTED_SHEET_NAME="{{instrumented_sheet_name}}"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
  
"""

def main():
    pipeline_filenames = create_pipelines()
    create_bash_runner(pipeline_filenames)

def create_pipelines():
    filenames = []
    for left_dataset_name in LEFT_DATASET_NAMES:
        for right_dataset_name in RIGHT_DATASET_NAMES:
            pipeline_content = Template(read_file_contents(PIPELINE_TEMPLATE)).render(left_dataset_name=left_dataset_name,
                                                                                      right_dataset_name=right_dataset_name,
                                                                                      left_column_name=RELATIONS[right_dataset_name][LEFT],
                                                                                      right_column_name=RELATIONS[right_dataset_name][RIGHT])
            print("Pipeline content:", pipeline_content)
            filename = Template(OUTPUT_FILENAME_FORMAT).render(left_dataset_name=left_dataset_name,
                                                               right_dataset_name=right_dataset_name,
                                                               left_column_name=RELATIONS[right_dataset_name][LEFT],
                                                               right_column_name=RELATIONS[right_dataset_name][RIGHT])
            filenames.append(filename)
            print("Filename generated:", filename)
            write_file_contents(filename, pipeline_content)
    return filenames

def read_file_contents(filename):
    with open(filename, 'r') as file:
        return file.read()

def write_file_contents(filename, contents):
    with open(filename, 'w') as file:
        return file.write(contents)

def create_bash_runner(pipeline_filenames):
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

def get_output_filename(pipeline_filename):
    return f"{pipeline_filename.split('-')[0]}.xlsx"

def get_sheet_name(pipeline_filename):
    pattern = re.sub("{{[^}]+}}", "(.*)", OUTPUT_FILENAME_FORMAT)
    match = re.search(pattern, pipeline_filename)
    left_dataset_name, _, _, right_dataset_name = match.groups()
    return f"{DATASETS_MAPPING[left_dataset_name]} - {DATASETS_MAPPING[right_dataset_name]}"

if __name__ == "__main__":
    main()
