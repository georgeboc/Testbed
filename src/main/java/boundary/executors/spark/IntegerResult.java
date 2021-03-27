package boundary.executors.spark;

import boundary.executors.Result;
import lombok.Data;

@Data
public class IntegerResult implements Result {
    private final Long value;
}
