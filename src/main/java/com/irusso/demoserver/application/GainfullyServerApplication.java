package com.irusso.demoserver.application;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.irusso.demoserver.application.model.GainfullyServerConfiguration;
import com.irusso.demoserver.application.module.DaoModule;
import com.irusso.demoserver.application.module.ServiceModule;
import com.irusso.demoserver.resources.*;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.migrations.MigrationsBundle;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.jdbi.v3.core.Jdbi;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.FilterRegistration;
import java.util.EnumSet;

/**
 * Main Dropwizard application class for the Gainfully Server.
 * This class handles the initialization and configuration of the REST API.
 */
public class GainfullyServerApplication
    extends Application<GainfullyServerConfiguration> {

    public static void main(String[] args) throws Exception {
        new GainfullyServerApplication().run(args);
    }

    @Override
    public String getName() {
        return "gainfully-server";
    }

    @Override
    public void initialize(Bootstrap<GainfullyServerConfiguration> bootstrap) {
        // Add Flyway migrations bundle
        bootstrap.addBundle(new MigrationsBundle<>() {
            @Override
            public DataSourceFactory getDataSourceFactory(GainfullyServerConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
    }

    @Override
    public void run(GainfullyServerConfiguration configuration, Environment environment) {
        // Configure CORS for both application and admin contexts
        configureCors(environment);

        // Configure database
        final JdbiFactory factory = new JdbiFactory();
        final Jdbi jdbi = factory.build(environment, configuration.getDataSourceFactory(), "postgresql");

        // Create Guice injector with DAO and Service modules
        final Injector injector = Guice.createInjector(new DaoModule(jdbi), new ServiceModule());

        // Register health checks
        final HealthCheckResource healthCheck = new HealthCheckResource();
        environment.healthChecks().register("gainfully-server", healthCheck);

        // Register REST resources using Guice injection
        environment.jersey().register(injector.getInstance(AuthResource.class));
        environment.jersey().register(injector.getInstance(UserResource.class));
        environment.jersey().register(injector.getInstance(EmployerResource.class));
        environment.jersey().register(injector.getInstance(JobPostingResource.class));
        environment.jersey().register(injector.getInstance(ApplicationResource.class));
        environment.jersey().register(injector.getInstance(UserSkillResource.class));
        environment.jersey().register(injector.getInstance(UserFieldOfInterestResource.class));
        environment.jersey().register(injector.getInstance(UserGeographicalInterestResource.class));
        environment.jersey().register(injector.getInstance(UserJobTypeInterestResource.class));
        environment.jersey().register(injector.getInstance(EmploymentHistoryResource.class));
        environment.jersey().register(injector.getInstance(EmployerHistoryResource.class));
        environment.jersey().register(injector.getInstance(JobRequirementResource.class));
        environment.jersey().register(injector.getInstance(SavedJobResource.class));
        environment.jersey().register(injector.getInstance(ApplicationMessageResource.class));
        environment.jersey().register(injector.getInstance(UserExperienceResource.class));
        environment.jersey().register(injector.getInstance(CityResource.class));
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

