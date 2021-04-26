package com.testbed.interactors;

import com.testbed.boundary.configurations.FrameworkConfiguration;
import com.testbed.boundary.parameters.InputParametersParserFactory;
import com.testbed.entities.parameters.InputParameters;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.text.SimpleDateFormat;
import java.util.Date;

@RequiredArgsConstructor
public class Application {
    static {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-hh_mm_ss-SS");
        System.setProperty("current_timestamp", dateFormat.format(new Date()));
    }
    private static final Logger LOG = LoggerFactory.getLogger(Application.class.getName());

    public void run(String[] arguments) throws Exception {
        LOG.info("Parsing parameters");
        InputParameters inputParameters = InputParametersParserFactory.getParametersParser().getParameters(arguments);
        LOG.info("Parameters parsed: {}", inputParameters);
        LOG.info("Initializing application");
        FrameworkConfiguration frameworkConfiguration = inputParameters.getFrameworkConfiguration();
        ApplicationContext context = new ClassPathXmlApplicationContext(frameworkConfiguration.getConfigurationFile());
        LOG.info("Application successfully initialized");
        LOG.info("Creating selected interactor");
        Interactor interactor = BeanFactoryAnnotationUtils.qualifiedBeanOfType(context.getAutowireCapableBeanFactory(),
                Interactor.class,
                inputParameters.getFrameworkConfiguration().getInteractorType());
        LOG.info("Executing selected interactor");
        interactor.execute(inputParameters);
        LOG.info("Interactor executed successfully");
        LOG.info("Application finished successfully");
    }
}
