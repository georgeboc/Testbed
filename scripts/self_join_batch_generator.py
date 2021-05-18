#/bin/python3
# Execute from Testbed root directory

from batch_generators_commons import *
from jinja2 import Template

PIPELINE_TEMPLATE = f"{SCRIPTS_TEMPLATES}/self_join_pipeline.json.template"

OUTPUT_FILENAME_FORMAT = "self_join_pipeline-column_number-{{column_index}}-{{dataset_name}}.json"
OUTPUT_BATCH_RUNNER_FILENAME = f"{SCRIPTS}/self_join_batch_runner.sh"

DATASETS_COLUMN_NAMES_MAPPING = {
    "Ad_click_on_taobao_512m": ["User", "DateTime"],
    "Ad_click_on_taobao_1g": ["User", "DateTime"]
}

def main():
    pipeline_filenames = []
    for dataset_name in DATASETS_COLUMN_NAMES_MAPPING.keys():
        pipeline_filenames.extend(create_pipelines(dataset_name))
    create_bash_runner(pipeline_filenames)

def create_pipelines(dataset_name):
    filenames = []
    for column_index, column_name in enumerate(DATASETS_COLUMN_NAMES_MAPPING[dataset_name]):
        pipeline_content = Template(read_file_contents(PIPELINE_TEMPLATE)).render(dataset_name=dataset_name,
                                                                                  column_name=column_name)
        print("Pipeline content:", pipeline_content)
        filename = Template(OUTPUT_FILENAME_FORMAT).render(dataset_name=dataset_name,
                                                           column_index=column_index)
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
    column_index_string, dataset_name = get_jinja_variables(pipeline_filename, OUTPUT_FILENAME_FORMAT)
    return f"{DATASETS_MAPPING[dataset_name]} | col. #{int(column_index_string) + 1}"

if __name__ == "__main__":
    main()
