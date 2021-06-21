#/bin/python3
# Execute from Testbed root directory

from batch_generators_commons import *
from dataclasses import dataclass
from jinja2 import Template
from typing import List

PIPELINE_TEMPLATE = f"{SCRIPTS_TEMPLATES}/select_project_pipeline.json.template"

OUTPUT_FILENAME_FORMAT = "select_project_pipeline-{{projected_columns_count}}_projected_columns_{{selectivity_" + \
                         "factor_percentage}}_percent_sf_{{dataset_name}}.json"
OUTPUT_BATCH_RUNNER_FILENAME = f"{SCRIPTS}/select_project_batch_runner.sh"

@dataclass
class DatasetInformation:
    column_selectivity_factor_percentages: List[int]
    columns_count: int
    selection_column_name: str

SELECTIVITY_FACTOR_PERCENTAGES = [1, 5, 20, 50, 100]

DATASET_INFORMATIONS = {
    "Ad_click_on_taobao_1g": DatasetInformation(column_selectivity_factor_percentages=
                                                [16.67, 33.34, 50, 66.67, 83.34, 100],
                                                columns_count=6,
                                                selection_column_name="DateTime"),
    "Obama_visitor_logs_1g": DatasetInformation(column_selectivity_factor_percentages=
                                                [3.58, 7.15, 10.72, 17.86, 35.72, 53.58, 71.43, 89.29, 100],
                                                columns_count=28,
                                                selection_column_name="NAMELAST"),
    "Thunderbird_30g": DatasetInformation(column_selectivity_factor_percentages=
                                          [14.29, 28.58, 42.86, 57.15, 71.43, 85.72, 100],
                                          columns_count=7,
                                          selection_column_name="Content")
}

def main():
    pipeline_filenames = [pipeline_filename
                          for dataset_name in DATASET_INFORMATIONS.keys()
                          for pipeline_filename in create_pipelines(dataset_name)]
    create_bash_runner(pipeline_filenames)

def create_pipelines(dataset_name):
    filenames = []
    for selectivity_factor_percentage in SELECTIVITY_FACTOR_PERCENTAGES:
        for column_selectivity_factor_percentage in DATASET_INFORMATIONS[dataset_name].column_selectivity_factor_percentages:
            column_selectivity_factor = get_normalized_column_selectivity_factor(column_selectivity_factor_percentage)
            selectivity_factor = get_normalized_selectivity_factor(selectivity_factor_percentage)
            selection_column_name = DATASET_INFORMATIONS[dataset_name].selection_column_name
            pipeline_template_content = read_file_contents(PIPELINE_TEMPLATE)
            pipeline_content = Template(pipeline_template_content).render(dataset_name=dataset_name,
                                                                          column_selectivity_factor=column_selectivity_factor,
                                                                          selectivity_factor=selectivity_factor,
                                                                          selection_column_name=selection_column_name)
            projected_columns_count = get_projected_columns_count(column_selectivity_factor_percentage,
                                                                  dataset_name,
                                                                  DATASET_INFORMATIONS)
            print("Pipeline content:", pipeline_content)
            filename = Template(OUTPUT_FILENAME_FORMAT).render(dataset_name=dataset_name,
                                                               projected_columns_count=projected_columns_count,
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
                                                   timed_sheet_name=get_timed_sheet_name(sheet_name),
                                                   instrumented_sheet_name=get_instrumented_sheet_name(sheet_name))
        batches += batch_entry
    batch_runner_content = Template(read_file_contents(BATCH_RUNNER_TEMPLATE)).render(batches=batches)
    write_file_contents(OUTPUT_BATCH_RUNNER_FILENAME, batch_runner_content)

def get_sheet_name(pipeline_filename):
    projected_columns_count, selectivity_factor_percentage, dataset_name = get_jinja_variables(pipeline_filename,
                                                                                               OUTPUT_FILENAME_FORMAT)
    return f"{DATASETS_MAPPING[dataset_name]}|{selectivity_factor_percentage}|{projected_columns_count}c"

if __name__ == "__main__":
    main()
