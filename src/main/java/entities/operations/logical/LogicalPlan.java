package entities.operations.logical;

import com.google.common.graph.Graph;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@SuppressWarnings("UnstableApiUsage")
public class LogicalPlan {
    private final Graph<LogicalOperation> graph;
    private final List<LogicalLoad> logicalLoads;
}
