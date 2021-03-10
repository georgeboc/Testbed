package entities.profiles;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FloatColumnarProfile implements ColumnarProfile {
    private final double max;
    private final double min;
}
