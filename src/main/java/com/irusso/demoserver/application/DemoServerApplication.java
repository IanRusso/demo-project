package com.irusso.demoserver.application;

import com.irusso.demoserver.application.model.DemoServerConfiguration;
import com.irusso.demoserver.resources.HealthCheckResource;
import com.irusso.demoserver.resources.UserResource;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.FilterRegistration;
import java.util.EnumSet;

/**
 * Main Dropwizard application class for the Demo Server.
 * This class handles the initialization and configuration of the REST API.
 */
public class DemoServerApplication extends Application<DemoServerConfiguration> {

    public static void main(String[] args) throws Exception {
        new DemoServerApplication().run(args);
    }

    @Override
    public String getName() {
        return "demo-server";
    }

    @Override
    public void initialize(Bootstrap<DemoServerConfiguration> bootstrap) {
        // Add any bundles or commands here
    }

    @Override
    public void run(DemoServerConfiguration configuration, Environment environment) {
        // Configure CORS for both application and admin contexts
        configureCors(environment);

        // Register health checks
        final HealthCheckResource healthCheck = new HealthCheckResource();
        environment.healthChecks().register("demo-server", healthCheck);

        // Register REST resources
        final UserResource userResource = new UserResource();
        environment.jersey().register(userResource);
    }

    private void configureCors(Environment environment) {
        // Configure CORS for application context (port 8080)
        final FilterRegistration.Dynamic cors = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
        cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "OPTIONS,GET,PUT,POST,DELETE,HEAD");
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

        // Configure CORS for admin context (port 8081)
        final FilterRegistration.Dynamic adminCors = environment.admin().addFilter("CORS", CrossOriginFilter.class);
        adminCors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        adminCors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin");
        adminCors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "OPTIONS,GET,PUT,POST,DELETE,HEAD");
        adminCors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
    }
}

