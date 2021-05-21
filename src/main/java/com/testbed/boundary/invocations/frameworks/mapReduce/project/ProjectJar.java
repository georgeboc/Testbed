package com.testbed.boundary.invocations.frameworks.mapReduce.project;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.example.data.simple.SimpleGroup;
import org.apache.parquet.schema.GroupType;
import org.apache.parquet.schema.Type;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectJar {
    private static final String PROJECTED_COLUMN_INDEXES = "projectedColumnIndexes";

    public static class ProjectMapper extends Mapper<LongWritable, Group, LongWritable, Group> {
        private static final String PROJECTED_COLUMNS = "ProjectedColumns";
        private static final int DEFAULT_POSITION = 0;

        @Override
        public void map(final LongWritable key, final Group value, final Context context) throws IOException, InterruptedException {
            int[] projectedColumnIndexes = context.getConfiguration().getInts(PROJECTED_COLUMN_INDEXES);
            GroupType originalGroupType = value.getType();
            List<Type> projectedColumns = getProjectedColumns(projectedColumnIndexes, originalGroupType);
            Group projectedGroup = createProjectedGroup(originalGroupType, projectedColumns);
            writeProjectedColumnsToGroup(value, projectedColumns, projectedGroup);
            context.write(key, projectedGroup);
        }

        private List<Type> getProjectedColumns(final int[] projectedColumnIndexes, final GroupType originalGroupType) {
            return Arrays.stream(projectedColumnIndexes)
                    .mapToObj(originalGroupType::getType)
                    .collect(Collectors.toList());
        }

        private Group createProjectedGroup(final GroupType originalGroupType, final List<Type> projectedColumns) {
            GroupType projectedGroupType = new GroupType(originalGroupType.getRepetition(),
                    PROJECTED_COLUMNS,
                    projectedColumns);
            return new SimpleGroup(projectedGroupType);
        }

        private void writeProjectedColumnsToGroup(final Group value,
                                                  final List<Type> projectedColumns,
                                                  final Group projectedGroup) {
            projectedColumns.stream()
                    .map(Type::getName)
                    .forEach(projectedColumnName -> projectedGroup.append(projectedColumnName,
                            value.getString(projectedColumnName, DEFAULT_POSITION)));
        }
    }
}
