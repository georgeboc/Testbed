package com.testbed.entities.profiles;

import com.testbed.entities.operations.logical.LogicalOperation;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProfileEstimation {
    private final LogicalOperation logicalOperation;
    private final Profile profile;
    private final String columnStatsPath;
}
