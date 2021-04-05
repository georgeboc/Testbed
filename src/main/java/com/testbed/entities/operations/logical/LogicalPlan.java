package com.testbed.entities.operations.logical;

import com.google.common.graph.Graph;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
@Data
@Builder
public class LogicalPlan {
    private final Graph<LogicalOperation> graph;
    private final List<LogicalLoad> logicalLoads;
}
