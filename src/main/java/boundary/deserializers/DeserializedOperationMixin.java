package boundary.deserializers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import entities.operations.deserialized.DeserializedLoad;
import entities.operations.deserialized.DeserializedSelect;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "operation")
@JsonSubTypes({
        @JsonSubTypes.Type(value = DeserializedLoad.class, name = "LOAD"),
        @JsonSubTypes.Type(value = DeserializedSelect.class, name = "SELECT")
})
public interface DeserializedOperationMixin {
}
