package entities.profiles;

import lombok.RequiredArgsConstructor;

import java.time.Instant;

@RequiredArgsConstructor
public class TimestampColumnarProfile implements ColumnarProfile {
    private final Instant max;
    private final Instant min;
}
