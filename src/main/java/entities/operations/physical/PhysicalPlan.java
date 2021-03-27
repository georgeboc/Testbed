package entities.operations.physical;

import com.google.common.graph.Graph;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@SuppressWarnings("UnstableApiUsage")
public class PhysicalPlan {
    private final Graph<PhysicalOperation> graph;
    private final List<PhysicalLoad> loadOperations;
}
