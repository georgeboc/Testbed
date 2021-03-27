package boundary.executors.spark;

import boundary.executors.Executable;
import boundary.executors.OperationInput;
import boundary.executors.Result;
import com.clearspring.analytics.util.Preconditions;
import lombok.RequiredArgsConstructor;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class Sink implements Executable {
    @Override
    public Result execute(OperationInput operationInput) {
        Preconditions.checkArgument(operationInput.getInputResults().size() == 1, "Sink operation receives %d inputs from previous node, when it is expected to receive one", operationInput.getInputResults().size());

        List<Result> inputResults = new ArrayList<>(operationInput.getInputResults());
        Result inputResult = inputResults.get(0);
        Dataset<Row> inputDataset = (Dataset<Row>) inputResult.getValue();
        return new IntegerResult(inputDataset.count());
    }
}
