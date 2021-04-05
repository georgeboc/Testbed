package com.testbed.interactors.validators.syntactic;

import com.testbed.entities.operations.deserialized.DeserializedOperations;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NotNullOnAllFieldsValidatorManager {
    private final NotNullOnAllFieldsValidator notNullOnAllFieldsValidator;

    public void validate(DeserializedOperations deserializedOperations) {
        deserializedOperations.forEach(notNullOnAllFieldsValidator::validate);
    }
}
