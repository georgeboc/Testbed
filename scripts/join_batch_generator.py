#/bin/python3
# Execute from Testbed root directory

from batch_generators_commons import *
from dataclasses import dataclass
from jinja2 import Template

PIPELINE_TEMPLATE = f"{SCRIPTS_TEMPLATES}/join_pipeline.json.template"

OUTPUT_FILENAME_FORMAT = "join_pipeline-{{left_dataset_name}}-{{left_column_name}}-{{right_column_name}}-{{right_dataset_name}}.json"
OUTPUT_BATCH_RUNNER_FILENAME = f"{SCRIPTS}/join_batch_runner.sh"

LEFT_DATASET_NAMES = ["Ad_click_on_taobao_512m", "Ad_click_on_taobao_1g"]
RIGHT_DATASET_NAMES = ["Ad_click_on_taobao_Ad_feature", "Ad_click_on_taobao_User_profile"]

@dataclass
class DatasetsRelation:
    left_column_name: str
    right_column_name: str

RELATIONS = {
    "Ad_click_on_taobao_Ad_feature": DatasetsRelation(left_column_name="AdGroupId", right_column_name="AdGroupId"),
    "Ad_click_on_taobao_User_profile": DatasetsRelation(left_column_name="User", right_column_name="UserId")
}

def main():
    pipeline_filenames = create_pipelines()
    create_bash_runner(pipeline_filenames)

def create_pipelines():
    filenames = []
    for left_dataset_name in LEFT_DATASET_NAMES:
        for right_dataset_name in RIGHT_DATASET_NAMES:
            left_column_name = RELATIONS[right_dataset_name].left_column_name
            right_column_name = RELATIONS[right_dataset_name].right_column_name
            pipeline_template_content = read_file_contents(PIPELINE_TEMPLATE)
            pipeline_content = Template(pipeline_template_content).render(left_dataset_name=left_dataset_name,
                                                                          right_dataset_name=right_dataset_name,
                                                                          left_column_name=left_column_name,
                                                                          right_column_name=right_column_name)
            print("Pipeline content:", pipeline_content)
            filename = Template(OUTPUT_FILENAME_FORMAT).render(left_dataset_name=left_dataset_name,
                                                               right_dataset_name=right_dataset_name,
                                                               left_column_name=left_column_name,
                                                               right_column_name=right_column_name)
            filenames.append(filename)
            print("Filename generated:", filename)
            write_file_contents(filename, pipeline_content)
    return filenames

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

def get_sheet_name(pipeline_filename):
    left_dataset_name, _, _, right_dataset_name = get_jinja_variables(pipeline_filename, OUTPUT_FILENAME_FORMAT)
    return f"{DATASETS_MAPPING[left_dataset_name]} - {DATASETS_MAPPING[right_dataset_name]}"

if __name__ == "__main__":
    main()
