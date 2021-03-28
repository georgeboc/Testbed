package com.testbed.boundary.executors.spark;

import com.testbed.boundary.executors.Result;
import lombok.Data;

@Data
public class IntegerResult implements Result {
    private final Long value;
}
