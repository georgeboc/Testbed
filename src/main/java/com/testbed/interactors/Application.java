package com.testbed.interactors;

import com.testbed.boundary.parameters.InputParametersParserFactory;
import com.testbed.entities.parameters.InputParameters;
import com.testbed.interactors.properties.ActiveProfilesPropertySetup;
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
    private static final String COMPONENT_SCAN_FILENAME = "ComponentScan.xml";

    public void run(final String[] arguments) throws Exception {
        LoggerPropertySetup.setup();
        Logger log = LoggerFactory.getLogger(Application.class.getName());
        log.info("Parsing parameters");
        InputParameters inputParameters = InputParametersParserFactory.getParametersParser().getParameters(arguments);
        log.info("Parameters parsed: {}", inputParameters);
        log.info("Setting up Environment Properties");
        EnvironmentPropertiesSetup.setup(inputParameters.isLocalEnvironment());
        log.info("Setting up Active Profiles Property");
        ActiveProfilesPropertySetup.setup(inputParameters.getFrameworkName(), inputParameters.isInstrumented());
        log.info("Creating Spring Context");
        ApplicationContext context = new ClassPathXmlApplicationContext(COMPONENT_SCAN_FILENAME);
        log.info("Creating selected interactor");
        Interactor interactor = BeanFactoryAnnotationUtils.qualifiedBeanOfType(context.getAutowireCapableBeanFactory(),
                Interactor.class,
                InteractorName.getInteractor(inputParameters.isInstrumented()).name());
        log.info("Executing selected interactor");
        interactor.execute(inputParameters);
        log.info("Interactor finished execution");
    }
}
