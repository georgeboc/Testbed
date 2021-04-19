package com.testbed.interactors.converters;

import com.testbed.entities.exceptions.TolerableErrorPercentageExceeded;

import static java.lang.Math.abs;

public class ConvertersCommons {
    public static void checkIfErrorIsTolerable(final double realValue,
                                         final double approximatedValue,
                                         final double tolerableErrorPercentage) {
        double absoluteImprecision = abs(realValue - approximatedValue);
        double relativeErrorPercentage = (absoluteImprecision/realValue)*100;
        if (relativeErrorPercentage >= tolerableErrorPercentage) {
            throw new TolerableErrorPercentageExceeded(relativeErrorPercentage, tolerableErrorPercentage);
        }
    }
}
