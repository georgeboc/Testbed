package entities.operations.physical;

import com.google.common.collect.Multimap;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PhysicalPlan {
    private final Multimap<PhysicalOperation, PhysicalOperation> graph;
    private final List<PhysicalLoad> loadOperations;
}
