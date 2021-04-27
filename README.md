# Testbed
This project is used to execute multiple experiments to compare Spark vs MapReduce.
Before creating a one-JAR with all dependencies, make sure the following dependencies
are installed:
- Maven
- Java JDK 11

Then, run the following script:
```bash
$ scripts/install.sh
```
Afterwards, it will create a `target` folder, which holds the one-JAR with all dependencies.

To run the JAR, use the following command:
```bash
$ java -jar Testbed-1.0-SNAPSHOT-jar-with-dependencies.jar [options]
```
Where `[options]` are the following:

```
usage: java -jar Testbed-1.0-SNAPSHOT-jar-with-dependencies.jar [options]
 -f,--framework-configuration <arg>      Data Processing Framework
                                         configuration. Available options
                                         are: [TimedMapReduce, TimedSpark,
                                         InstrumentedMapReduce,
                                         InstrumentedSpark]
 -l,--local                              If the flag is present, the
                                         Testbed uses the local
                                         environment for the frameworks.
                                         Without this flag, the Testbed
                                         uses the cluster environment.
 -o,--output <arg>                       Output file path
 -p,--pipeline <arg>                     Pipeline file path
 -s,--sheet-name <arg>                   Sheet name in output file path
 -t,--tolerable-error-percentage <arg>   Tolerable error percentage.
                                         Default value is: 5.0
```

This section is extracted from usage, which can always be displayed when invoking
the java JAR with no options, as follows:
```bash
$ java -jar Testbed-1.0-SNAPSHOT-jar-with-dependencies.jar
```

The pipeline refers to a JSON file with the following aspect:
```JSON
[
  {
    "operation": "LOAD",
    "outputTag": "dataset1",
    "datasetDirectoryPath": "input/Ad_click_on_taobao_10000"
  },
  {
    "operation": "SELECT",
    "inputTag": "dataset1",
    "outputTag": "selectedDataset1",
    "rowsSelectivityFactor": 0.005,
    "columnName": "DateTime"
  }
]
```

An example of invocation with inputParameters is the following:
```bash
$ java -jar Testbed-1.0-SNAPSHOT-jar-with-dependencies.jar \
--tolerable-error-percentage 5.0 \
--framework-configuration InstrumentedMapReduce \
--pipeline pipelines/pipeline.json \
--output output/operation_instrumentations.xlsx \
--sheet-name Dataset_5% \
--local
```

[comment]: # (TODO: Add link)
For more information, check the thesis associated to this project.