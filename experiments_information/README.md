In this directory, we have put the followign subdirectories:

- cardinality_calculations: here we put the estimations of big join, join and select-project operations and the file distribution we had in HDFS.
- pipelines: here we put the auto-generated pipelines, that one can generate by executing the following command from the Testbed folder: `$ python3 scripts/<pipeline_name>_batch_generatos.py`, where `<pipeline_name>` has to be replaced by pipeline's name.
- raw_excels: These are the outputs form out Testbed
- processed_excels: Here we put refactored the raw_excels and added graphs to them, which we used in the dissertation.
- runner_scripts: here we put the auto-generated runner scripts; to run an experiment, it is as simple as puttig these scripts under the scripts folder and executing this command from the Testbed folder: `$ bash scripts/<pipeline_name>_batch_runner.sh`, where `<pipeline_name>` is pipeline's name.
- utils: in this folder we have included an excel used to calculate the parameters for Hadoop, Spark and MapReduce, which we used to tune the cluster.
