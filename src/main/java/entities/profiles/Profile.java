package entities.profiles;

import lombok.Data;

import java.util.Map;

@Data
public class Profile {
    private final Map<String, ColumnProfile> columns;
}
