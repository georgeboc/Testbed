package com.testbed.boundary.deserializers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.testbed.entities.operations.deserialized.DeserializedAggregate;
import com.testbed.entities.operations.deserialized.DeserializedGroupBy;
import com.testbed.entities.operations.deserialized.DeserializedJoin;
import com.testbed.entities.operations.deserialized.DeserializedLoad;
import com.testbed.entities.operations.deserialized.DeserializedProject;
import com.testbed.entities.operations.deserialized.DeserializedSelect;
import com.testbed.entities.operations.deserialized.DeserializedUnion;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "operation")
@JsonSubTypes({
        @JsonSubTypes.Type(value = DeserializedLoad.class, name = "LOAD"),
        @JsonSubTypes.Type(value = DeserializedSelect.class, name = "SELECT"),
        @JsonSubTypes.Type(value = DeserializedProject.class, name = "PROJECT"),
        @JsonSubTypes.Type(value = DeserializedJoin.class, name = "JOIN"),
        @JsonSubTypes.Type(value = DeserializedGroupBy.class, name = "GROUP BY"),
        @JsonSubTypes.Type(value = DeserializedAggregate.class, name = "AGGREGATE"),
        @JsonSubTypes.Type(value = DeserializedUnion.class, name = "UNION")
})
public interface DeserializedOperationMixin {
}
