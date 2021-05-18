#/bin/python3
# Execute from Testbed root directory

from batch_generators_commons import *
from jinja2 import Template

PIPELINE_TEMPLATE = f"{SCRIPTS_TEMPLATES}/select_pipeline.json.template"
BATCH_RUNNER_TEMPLATE = f"{SCRIPTS_TEMPLATES}/batch_runner.sh.template"

OUTPUT_FILENAME_FORMAT = "select_pipeline-{{selectivity_factor_percentage}}_percent_{{dataset_name}}.json"
OUTPUT_BATCH_RUNNER_FILENAME = f"{SCRIPTS}/select_batch_runner.sh"

DATASET_NAMES_AND_COLUMN_NAMES = {
    "Ad_click_on_taobao_512m": "DateTime",
    "Ad_click_on_taobao_1g": "DateTime",
    "Obama_visitor_logs_1g": "NAMELAST",
    "Thunderbird_30g": "Content"
}

SELECTIVITY_FACTOR_PERCENTAGES = [1, 3, 5, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100]

def main():
    pipeline_filenames = []
    for dataset_name, column_name in DATASET_NAMES_AND_COLUMN_NAMES.items():
        pipeline_filenames.extend(create_pipelines(column_name, dataset_name))
    create_bash_runner(pipeline_filenames)

def create_pipelines(column_name, dataset_name):
    filenames = []
    for selectivity_factor_percentage in SELECTIVITY_FACTOR_PERCENTAGES:
        pipeline_content = Template(read_file_contents(PIPELINE_TEMPLATE)).render(dataset_name=dataset_name,
                                                                 selectivity_factor=get_normalized_selectivity_factor(
                                                                         selectivity_factor_percentage),
                                                                 column_name=column_name)
        print("Pipeline content:", pipeline_content)
        filename = Template(OUTPUT_FILENAME_FORMAT).render(dataset_name=dataset_name,
                                                           selectivity_factor_percentage=selectivity_factor_percentage)
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
    percentage_string, dataset_name = get_jinja_variables(pipeline_filename, OUTPUT_FILENAME_FORMAT)
    return f"{DATASETS_MAPPING[dataset_name]} | {percentage_string}% SF"

if __name__ == "__main__":
    main()
