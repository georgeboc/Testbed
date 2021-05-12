#/bin/python3
# Execute from Testbed root directory

from jinja2 import Template

TEMPLATE = "select_pipeline.json.template"

OUTPUT_FILENAME_FORMAT = "select_pipeline-{{selectivity_factor_percentage}}_percent_{{dataset_name}}.json"

selectivity_factor_percentages = [1, 3, 5, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100]

def get_normalized_selectivity_factor(selectivity_factor_percentage):
    return selectivity_factor_percentage/100.0

def read_template_contents():
    with open(TEMPLATE, 'r') as file:
        return file.read()

def write_pipeline_contents(filename, contents):
    with open(filename, 'w') as file:
        return file.write(contents)

def main():
    dataset_name = input("Insert Dataset Name: ")
    column_name = input("Column Name: ")
    for selectivity_factor_percentage in selectivity_factor_percentages:
        pipeline_content = Template(read_template_contents()).render(dataset_name=dataset_name, selectivity_factor=get_normalized_selectivity_factor(selectivity_factor_percentage), column_name=column_name)
        print("Pipeline content:", pipeline_content)
        filename = Template(OUTPUT_FILENAME_FORMAT).render(dataset_name=dataset_name, selectivity_factor_percentage=selectivity_factor_percentage)
        print("Filename generated:", filename)
        write_pipeline_contents(filename, pipeline_content)

main()
