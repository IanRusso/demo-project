package com.irusso.demoserver.application.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.irusso.demoserver.db.dao.*;
import org.jdbi.v3.core.Jdbi;

/**
 * Guice module for Data Access Object (DAO) dependency injection.
 * This module provides singleton instances of all DAOs in the application.
 */
public class DaoModule extends AbstractModule {

    private final Jdbi jdbi;

    /**
     * Constructor for DaoModule.
     *
     * @param jdbi The JDBI instance to be injected into DAOs
     */
    public DaoModule(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    protected void configure() {
        // Bind the Jdbi instance as a singleton
        bind(Jdbi.class).toInstance(jdbi);
    }

    /**
     * Provides a singleton instance of UserDao.
     */
    @Provides
    @Singleton
    public UserDao provideUserDao(Jdbi jdbi) {
        return new UserDao(jdbi);
    }

    /**
     * Provides a singleton instance of UserSkillDao.
     */
    @Provides
    @Singleton
    public UserSkillDao provideUserSkillDao(Jdbi jdbi) {
        return new UserSkillDao(jdbi);
    }

    /**
     * Provides a singleton instance of UserFieldOfInterestDao.
     */
    @Provides
    @Singleton
    public UserFieldOfInterestDao provideUserFieldOfInterestDao(Jdbi jdbi) {
        return new UserFieldOfInterestDao(jdbi);
    }

    /**
     * Provides a singleton instance of UserGeographicalInterestDao.
     */
    @Provides
    @Singleton
    public UserGeographicalInterestDao provideUserGeographicalInterestDao(Jdbi jdbi) {
        return new UserGeographicalInterestDao(jdbi);
    }

    /**
     * Provides a singleton instance of UserJobTypeInterestDao.
     */
    @Provides
    @Singleton
    public UserJobTypeInterestDao provideUserJobTypeInterestDao(Jdbi jdbi) {
        return new UserJobTypeInterestDao(jdbi);
    }

    /**
     * Provides a singleton instance of EmploymentHistoryDao.
     */
    @Provides
    @Singleton
    public EmploymentHistoryDao provideEmploymentHistoryDao(Jdbi jdbi) {
        return new EmploymentHistoryDao(jdbi);
    }

    /**
     * Provides a singleton instance of EmployerDao.
     */
    @Provides
    @Singleton
    public EmployerDao provideEmployerDao(Jdbi jdbi) {
        return new EmployerDao(jdbi);
    }

    /**
     * Provides a singleton instance of EmployerHistoryDao.
     */
    @Provides
    @Singleton
    public EmployerHistoryDao provideEmployerHistoryDao(Jdbi jdbi) {
        return new EmployerHistoryDao(jdbi);
    }

    /**
     * Provides a singleton instance of JobPostingDao.
     */
    @Provides
    @Singleton
    public JobPostingDao provideJobPostingDao(Jdbi jdbi) {
        return new JobPostingDao(jdbi);
    }

    /**
     * Provides a singleton instance of JobRequirementDao.
     */
    @Provides
    @Singleton
    public JobRequirementDao provideJobRequirementDao(Jdbi jdbi) {
        return new JobRequirementDao(jdbi);
    }

    /**
     * Provides a singleton instance of SavedJobDao.
     */
    @Provides
    @Singleton
    public SavedJobDao provideSavedJobDao(Jdbi jdbi) {
        return new SavedJobDao(jdbi);
    }

    /**
     * Provides a singleton instance of ApplicationDao.
     */
    @Provides
    @Singleton
    public ApplicationDao provideApplicationDao(Jdbi jdbi) {
        return new ApplicationDao(jdbi);
    }

    /**
     * Provides a singleton instance of ApplicationMessageDao.
     */
    @Provides
    @Singleton
    public ApplicationMessageDao provideApplicationMessageDao(Jdbi jdbi) {
        return new ApplicationMessageDao(jdbi);
    }
}

