package com.testbed.entities.exceptions;

public class TolerableErrorPercentageExceeded extends RuntimeException {
    public TolerableErrorPercentageExceeded(double actualErrorPercentage, double tolerableErrorPercentage) {
        super(String.format("Tolerable error of %.2f%% has been exceeded. The actual error is %.2f%%",
                tolerableErrorPercentage,
                actualErrorPercentage));
    }
}
