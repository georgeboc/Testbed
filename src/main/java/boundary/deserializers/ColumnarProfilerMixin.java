package boundary.deserializers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import entities.profiles.FloatColumnarProfile;
import entities.profiles.IntegerColumnarProfile;
import entities.profiles.StringColumnarProfile;
import entities.profiles.TimestampColumnarProfile;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = FloatColumnarProfile.class, name = "Float"),
        @JsonSubTypes.Type(value = IntegerColumnarProfile.class, name = "Integer"),
        @JsonSubTypes.Type(value = StringColumnarProfile.class, name = "String"),
        @JsonSubTypes.Type(value = TimestampColumnarProfile.class, name = "Timestamp")
})
public interface ColumnarProfilerMixin {
}
