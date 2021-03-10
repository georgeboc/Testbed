package entities.profiles;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IntegerColumnarProfile implements ColumnarProfile {
    private final long max;
    private final long min;
}
