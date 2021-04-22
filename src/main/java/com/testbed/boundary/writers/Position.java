package com.testbed.boundary.writers;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Position {
    private final int row;
    private final int column;
}
