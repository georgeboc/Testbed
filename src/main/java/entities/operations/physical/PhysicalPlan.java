package entities.operations.physical;

import com.google.common.collect.Multimap;
import lombok.Data;

import java.util.List;

@Data
public class PhysicalPlan {
    private final Multimap<PhysicalOperation, PhysicalOperation> graph;
    private final List<PhysicalLoad> loadOperations;
}
