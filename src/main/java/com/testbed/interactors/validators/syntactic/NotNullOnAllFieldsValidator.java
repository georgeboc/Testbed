package com.testbed.interactors.validators.syntactic;

import com.clearspring.analytics.util.Preconditions;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class NotNullOnAllFieldsValidator {
    private static final boolean PRIVATE_FIELDS_ACCESSIBLES = true;

    public void validate(Object object) {
        List<String> nullFields = getNullFields(object);
        Preconditions.checkArgument(nullFields.isEmpty(),
                "Fields: %s are null in object %s, although they are expected to have a value",
                nullFields, object.getClass().getSimpleName());
    }

    private List<String> getNullFields(Object object) {
        return Arrays.stream(object.getClass().getDeclaredFields())
                .filter(field -> Objects.isNull(tryGetFieldValueFromObject(field, object)))
                .map(Field::getName)
                .collect(Collectors.toList());
    }

    private Object tryGetFieldValueFromObject(Field field, Object object) {
        try {
            field.setAccessible(PRIVATE_FIELDS_ACCESSIBLES);
            return field.get(object);
        } catch (IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }
}
