package com.testbed.entities.jobs;

import lombok.Data;

import java.util.List;

@Data
public class Job {
    private final List<JobOperation> jobOperations;
}
