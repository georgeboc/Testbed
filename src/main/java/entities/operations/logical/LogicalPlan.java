package entities.operations.logical;

import com.google.common.collect.Multimap;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LogicalPlan {
    private final Multimap<LogicalOperation, LogicalOperation> graph;
    private final List<LogicalLoad> logicalLoads;
}
