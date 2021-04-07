package com.testbed.entities.exceptions;

public class TolerableErrorPercentageExceeded extends RuntimeException {
    public TolerableErrorPercentageExceeded(final double actualErrorPercentage, final double tolerableErrorPercentage) {
        super(String.format("The actual error of %.2f%% is greater than the tolerable error of %.2f%%",
                actualErrorPercentage,
                tolerableErrorPercentage));
    }
}
