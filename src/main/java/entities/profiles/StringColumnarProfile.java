package entities.profiles;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StringColumnarProfile implements ColumnarProfile {
    private final String max;
    private final String min;
}
