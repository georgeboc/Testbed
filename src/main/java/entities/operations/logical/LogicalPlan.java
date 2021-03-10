package entities.operations.logical;

import com.google.common.collect.Multimap;
import lombok.Data;

import java.util.List;

@Data
public class LogicalPlan {
    private final Multimap<LogicalOperation, LogicalOperation> graph;
    private final List<LogicalLoad> loadOperations;
}
