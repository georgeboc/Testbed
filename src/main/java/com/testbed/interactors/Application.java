package com.testbed.interactors;

import com.testbed.boundary.parameters.ParametersParserFactory;
import com.testbed.entities.parameters.Parameters;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@RequiredArgsConstructor
public class Application {
    private static final Logger LOG = LoggerFactory.getLogger(Application.class.getName());

    public void run(String[] arguments) throws Exception {
        LOG.info("Parsing parameters");
        Parameters parameters = ParametersParserFactory.getParametersParser().getParameters(arguments);
        LOG.info("Parameters parsed: " + parameters);
        LOG.info("Initializing application");
        ApplicationContext context = new ClassPathXmlApplicationContext(parameters.getConfiguration().getConfigurationFile());
        LOG.info("Application successfully initialized");
        LOG.info("Creating selected interactor");
        Interactor interactor = BeanFactoryAnnotationUtils.qualifiedBeanOfType(context.getAutowireCapableBeanFactory(),
                Interactor.class,
                parameters.getConfiguration().toString());
        LOG.info("Executing selected interactor");
        interactor.execute(parameters);
        LOG.info("Interactor executed successfully");
        LOG.info("Application finished successfully");
    }
}
