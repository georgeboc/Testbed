import re

SCRIPTS = "scripts"
SCRIPTS_TEMPLATES = f"{SCRIPTS}/templates"
SCRIPTS = "scripts"
PIPELINES = "pipelines"

LEFT = 0
RIGHT = 1

DATASETS_MAPPING = {
    "Ad_click_on_taobao_512m": "little (512M)",
    "Ad_click_on_taobao_1g": "little (1G)",
    "Ad_click_on_taobao_Ad_feature": "AdFeature",
    "Ad_click_on_taobao_User_profile": "UserProfile",
    "Obama_visitor_logs_1g": "middle (1G)",
    "Thunderbird_30g": "big (30G)",
}

BATCH_RUNNER_TEMPLATE = f"{SCRIPTS_TEMPLATES}/batch_runner.sh.template"

BATCH_ENTRY = """  
  PIPELINE="hdfs://dtim:27000/user/bochileanu/pipelines/{{pipeline_filename}}"
  OUTPUT="hdfs://dtim:27000/user/bochileanu/analysis_results/{{output_filename}}"
  SHEET_NAME="{{sheet_name}}"
  INSTRUMENTED_SHEET_NAME="{{instrumented_sheet_name}}"
  bash -c "$EXPERIMENTS_RUNNER_SCRIPT_PATH '$PIPELINE' '$OUTPUT' '$SHEET_NAME' '$INSTRUMENTED_SHEET_NAME' '5'"
  
"""

def read_file_contents(filename):
    with open(filename, 'r') as file:
        return file.read()

def write_file_contents(filename, contents):
    with open(filename, 'w') as file:
        return file.write(contents)

def get_instrumented_sheet_name(sheet_name):
    return f"{sheet_name} Ins."

def get_output_filename(pipeline_filename):
    return f"{pipeline_filename.split('-')[0]}.xlsx"

def get_jinja_variables(pipeline_filename, output_filename_format):
    pattern = re.sub("{{[^}]+}}", "(.*)", output_filename_format)
    return re.search(pattern, pipeline_filename).groups()

def get_normalized_selectivity_factor(selectivity_factor_percentage):
    return selectivity_factor_percentage/100.0

def get_normalized_column_selectivity_factor(column_selectivity_factor_percentage):
    return column_selectivity_factor_percentage / 100.0

def get_projected_columns_count(column_selectivity_factor_percentage,
                                dataset_name,
                                dataset_informations):
    return int(dataset_informations[dataset_name].columns_count * column_selectivity_factor_percentage / 100)
