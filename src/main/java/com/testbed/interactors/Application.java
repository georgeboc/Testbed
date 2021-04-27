package com.testbed.interactors;

import com.testbed.boundary.configurations.FrameworkConfiguration;
import com.testbed.boundary.parameters.InputParametersParserFactory;
import com.testbed.entities.parameters.InputParameters;
import com.testbed.interactors.properties.EnvironmentPropertiesSetup;
import com.testbed.interactors.properties.LoggerPropertySetup;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@RequiredArgsConstructor
public class Application {
    public void run(String[] arguments) throws Exception {
        LoggerPropertySetup.setup();
        Logger log = LoggerFactory.getLogger(Application.class.getName());
        log.info("Parsing parameters");
        InputParameters inputParameters = InputParametersParserFactory.getParametersParser().getParameters(arguments);
        log.info("Parameters parsed: {}", inputParameters);
        log.info("Setting up Environment Properties");
        EnvironmentPropertiesSetup.setup(inputParameters.isLocalEnvironment());
        log.info("Initializing application");
        FrameworkConfiguration frameworkConfiguration = inputParameters.getFrameworkConfiguration();
        ApplicationContext context = new ClassPathXmlApplicationContext(frameworkConfiguration.getFilename());
        log.info("Application successfully initialized");
        log.info("Creating selected interactor");
        Interactor interactor = BeanFactoryAnnotationUtils.qualifiedBeanOfType(context.getAutowireCapableBeanFactory(),
                Interactor.class,
                inputParameters.getFrameworkConfiguration().getInteractorType());
        log.info("Executing selected interactor");
        interactor.execute(inputParameters);
        log.info("Interactor executed successfully");
        log.info("Application finished successfully");
    }
}
