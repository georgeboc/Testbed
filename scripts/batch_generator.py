#/bin/python3
# Execute from Testbed root directory

import re
from jinja2 import Template

SCRIPTS = "scripts"
SCRIPTS_GENERATED = "scripts/generated"
PIPELINES = "pipelines"

PIPELINE_TEMPLATE = f"{SCRIPTS}/select_pipeline.json.template"
BATCH_RUNNER_TEMPLATE = f"{SCRIPTS}/batch_runner.sh.template"

OUTPUT_FILENAME_FORMAT = f"{PIPELINES}/select_pipeline-\\{{selectivity_factor_percentage\\}}_percent_\\{{dataset_name\\}}.json"
OUTPUT_BATCH_RUNNER_FILENAME = f"{SCRIPTS_GENERATED}/select_batch_runner.sh"

SELECTIVITY_FACTOR_PERCENTAGES = [1, 3, 5, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100]

BATCH_ENTRY = """  
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/{{pipeline_filename}}"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/output/{{output_filename}}"
  SHEET_NAME="{{sheet_name}}"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH $PIPELINE $OUTPUT $SHEET_NAME"
  
"""

def main():
    dataset_names_and_column_names = get_dataset_names_and_column_names()
    for dataset_name_and_column_name in dataset_names_and_column_names:
        dataset_name, column_name = dataset_name_and_column_name
        create_pipelines(column_name, dataset_name)
    dataset_names = get_dataset_names(dataset_names_and_column_names)
    create_bash_runner(dataset_names)

def create_pipelines(column_name, dataset_name):
    for selectivity_factor_percentage in SELECTIVITY_FACTOR_PERCENTAGES:
        pipeline_content = Template(read_file_contents(PIPELINE_TEMPLATE)).render(dataset_name=dataset_name,
                                                                 selectivity_factor=get_normalized_selectivity_factor(
                                                                         selectivity_factor_percentage),
                                                                 column_name=column_name)
        print("Pipeline content:", pipeline_content)
        filename = Template(OUTPUT_FILENAME_FORMAT).render(dataset_name=dataset_name,
                                                           selectivity_factor_percentage=selectivity_factor_percentage)
        print("Filename generated:", filename)
        write_file_contents(filename, pipeline_content)

def get_dataset_names_and_column_names():
    dataset_names_and_column_names = []
    while True:
        dataset_name = input("Insert Dataset Name: ")
        column_name = input("Column Name: ")
        dataset_names_and_column_names.append((dataset_name, column_name))
        if input("Insert another dataset and column name? (y/N): ") != "y":
            return dataset_names_and_column_names

def get_dataset_names(dataset_names_and_column_names):
    return [dataset_name_and_column_name[0] for dataset_name_and_column_name in dataset_names_and_column_names]

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
                                                   sheet_name=sheet_name)
        batches += batch_entry
    batch_runner_content = Template(read_file_contents(BATCH_RUNNER_TEMPLATE)).render(batches=batches)
    write_file_contents(OUTPUT_BATCH_RUNNER_FILENAME, batch_runner_content)

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
    return f"{dataset_name}_{percentage}_percent_selectivity_factor"

if __name__ == "__main__":
    main()
