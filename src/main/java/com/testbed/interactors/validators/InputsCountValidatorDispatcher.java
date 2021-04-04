package com.testbed.interactors.validators;

import com.testbed.entities.operations.logical.LogicalOperation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum InputsCountValidatorDispatcher {
    ZeroaryLogicalOperation(new ZeroaryInputsCountValidator()),
    UnaryLogicalOperation(new UnaryInputsCountValidator()),
    BinaryLogicalOperation(new BinaryInputsCountValidator());

    private final InputsCountValidator inputsCountValidator;

    public static InputsCountValidator dispatch(LogicalOperation logicalOperation) {
        return Arrays.stream(logicalOperation.getClass().getInterfaces())
                .map(Class::getSimpleName)
                .map(InputsCountValidatorDispatcher::valueOf)
                .map(InputsCountValidatorDispatcher::getInputsCountValidator)
                .findFirst()
                .get();
    }
}
