package com.testbed.entities.profiles;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ColumnProfile {
    private final long rowsCount;
    private final boolean isUnique;
}
