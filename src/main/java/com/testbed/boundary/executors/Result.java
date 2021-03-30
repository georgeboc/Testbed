package com.testbed.boundary.executors;

import java.util.List;

public interface Result {
    Object getValues();
    long count();
    List<String> getColumnNames();
}
