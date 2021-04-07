package com.testbed.springConfiguration;

import com.testbed.interactors.validators.semantic.BinaryInputsCountValidator;
import com.testbed.interactors.validators.semantic.InputsCountValidator;
import com.testbed.interactors.validators.semantic.InputsCountValidatorManager;
import com.testbed.interactors.validators.semantic.UnaryInputsCountValidator;
import com.testbed.interactors.validators.semantic.ZeroaryInputsCountValidator;
import com.testbed.interactors.validators.syntactic.NotNullOnAllFieldsValidator;
import com.testbed.interactors.validators.syntactic.NotNullOnAllFieldsValidatorManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.testbed.springConfiguration.LogicalToPhysicalConfiguration.LOGICAL_AGGREGATE;
import static com.testbed.springConfiguration.LogicalToPhysicalConfiguration.LOGICAL_GROUP_BY;
import static com.testbed.springConfiguration.LogicalToPhysicalConfiguration.LOGICAL_JOIN;
import static com.testbed.springConfiguration.LogicalToPhysicalConfiguration.LOGICAL_LOAD;
import static com.testbed.springConfiguration.LogicalToPhysicalConfiguration.LOGICAL_PROJECT;
import static com.testbed.springConfiguration.LogicalToPhysicalConfiguration.LOGICAL_SELECT;
import static com.testbed.springConfiguration.LogicalToPhysicalConfiguration.LOGICAL_UNION;

@Configuration
public class ValidatorsConfiguration {
    @Bean
    public InputsCountValidatorManager inputsCountValidatorManager() {
        return new InputsCountValidatorManager();
    }

    @Bean
    @Qualifier(LOGICAL_LOAD)
    public InputsCountValidator loadInputsCountValidator() {
        return new ZeroaryInputsCountValidator();
    }

    @Bean
    @Qualifier(LOGICAL_AGGREGATE)
    public InputsCountValidator aggregateInputsCountValidator() {
        return new UnaryInputsCountValidator();
    }

    @Bean
    @Qualifier(LOGICAL_GROUP_BY)
    public InputsCountValidator groupByInputsCountValidator() {
        return new UnaryInputsCountValidator();
    }

    @Bean
    @Qualifier(LOGICAL_PROJECT)
    public InputsCountValidator projectInputsCountValidator() {
        return new UnaryInputsCountValidator();
    }

    @Bean
    @Qualifier(LOGICAL_SELECT)
    public InputsCountValidator selectInputsCountValidator() {
        return new UnaryInputsCountValidator();
    }

    @Bean
    @Qualifier(LOGICAL_JOIN)
    public InputsCountValidator joinInputsCountValidator() {
        return new BinaryInputsCountValidator();
    }

    @Bean
    @Qualifier(LOGICAL_UNION)
    public InputsCountValidator unionInputsCountValidator() {
        return new BinaryInputsCountValidator();
    }

    @Bean
    public NotNullOnAllFieldsValidator notNullOnAllFieldsValidator() {
        return new NotNullOnAllFieldsValidator();
    }

    @Bean
    public NotNullOnAllFieldsValidatorManager notNullOnAllFieldsValidatorManager() {
        return new NotNullOnAllFieldsValidatorManager(notNullOnAllFieldsValidator());
    }
}
