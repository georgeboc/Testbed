package com.testbed.boundary.invocations.results;

import java.util.List;

public interface Result {
    Object getValues();
    long count();
    List<String> getColumnNames();
}
