package com.testbed.interactors.monitors;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MonitorNameParameters {
    private final String monitorNamePrefix;
    private final String monitorNameSuffix;
}
