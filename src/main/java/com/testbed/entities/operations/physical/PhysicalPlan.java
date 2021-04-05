package com.testbed.entities.operations.physical;

import com.google.common.graph.Graph;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
@Data
@Builder
public class PhysicalPlan {
    private final Graph<PhysicalOperation> graph;
    private final List<PhysicalLoad> loadOperations;
}
