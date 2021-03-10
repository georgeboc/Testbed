package entities.operations.deserialized;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeserializedLoad implements DeserializedOperation {
    private String datasetPath;
    private String datasetProfilePath;
    private String outputTag;

    @Override
    public String getInputTag() {
        return null;
    }
}
