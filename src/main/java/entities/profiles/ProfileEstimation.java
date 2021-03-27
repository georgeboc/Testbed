package entities.profiles;

import entities.operations.logical.LogicalOperation;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileEstimation {
    private final LogicalOperation logicalOperation;
    private final Profile profile;
    private final String columnStatsPath;
}
