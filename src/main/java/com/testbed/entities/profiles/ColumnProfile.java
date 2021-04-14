package com.testbed.entities.profiles;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ColumnProfile {
    private final long distinctRowsCount;
    private final long totalRowsCount;
    private final boolean isUnique;
}
