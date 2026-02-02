package com.irusso.demoserver.application.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.irusso.demoserver.db.dao.*;
import com.irusso.demoserver.service.*;

/**
 * Guice module for Service layer dependency injection.
 * This module provides singleton instances of all services in the application.
 */
public class ServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        // No explicit bindings needed - using @Provides methods
    }

    /**
     * Provides a singleton instance of UserService.
     */
    @Provides
    @Singleton
    public UserService provideUserService(UserDao userDao) {
        return new UserService(userDao);
    }

    /**
     * Provides a singleton instance of UserSkillService.
     */
    @Provides
    @Singleton
    public UserSkillService provideUserSkillService(UserSkillDao userSkillDao) {
        return new UserSkillService(userSkillDao);
    }

    /**
     * Provides a singleton instance of UserFieldOfInterestService.
     */
    @Provides
    @Singleton
    public UserFieldOfInterestService provideUserFieldOfInterestService(UserFieldOfInterestDao userFieldOfInterestDao) {
        return new UserFieldOfInterestService(userFieldOfInterestDao);
    }

    /**
     * Provides a singleton instance of UserGeographicalInterestService.
     */
    @Provides
    @Singleton
    public UserGeographicalInterestService provideUserGeographicalInterestService(UserGeographicalInterestDao userGeographicalInterestDao) {
        return new UserGeographicalInterestService(userGeographicalInterestDao);
    }

    /**
     * Provides a singleton instance of UserJobTypeInterestService.
     */
    @Provides
    @Singleton
    public UserJobTypeInterestService provideUserJobTypeInterestService(UserJobTypeInterestDao userJobTypeInterestDao) {
        return new UserJobTypeInterestService(userJobTypeInterestDao);
    }

    /**
     * Provides a singleton instance of EmploymentHistoryService.
     */
    @Provides
    @Singleton
    public EmploymentHistoryService provideEmploymentHistoryService(EmploymentHistoryDao employmentHistoryDao) {
        return new EmploymentHistoryService(employmentHistoryDao);
    }

    /**
     * Provides a singleton instance of EmployerService.
     */
    @Provides
    @Singleton
    public EmployerService provideEmployerService(EmployerDao employerDao) {
        return new EmployerService(employerDao);
    }

    /**
     * Provides a singleton instance of EmployerHistoryService.
     */
    @Provides
    @Singleton
    public EmployerHistoryService provideEmployerHistoryService(EmployerHistoryDao employerHistoryDao) {
        return new EmployerHistoryService(employerHistoryDao);
    }

    /**
     * Provides a singleton instance of JobPostingService.
     */
    @Provides
    @Singleton
    public JobPostingService provideJobPostingService(JobPostingDao jobPostingDao) {
        return new JobPostingService(jobPostingDao);
    }

    /**
     * Provides a singleton instance of JobRequirementService.
     */
    @Provides
    @Singleton
    public JobRequirementService provideJobRequirementService(JobRequirementDao jobRequirementDao) {
        return new JobRequirementService(jobRequirementDao);
    }

    /**
     * Provides a singleton instance of SavedJobService.
     */
    @Provides
    @Singleton
    public SavedJobService provideSavedJobService(SavedJobDao savedJobDao) {
        return new SavedJobService(savedJobDao);
    }

    /**
     * Provides a singleton instance of ApplicationService.
     */
    @Provides
    @Singleton
    public ApplicationService provideApplicationService(ApplicationDao applicationDao) {
        return new ApplicationService(applicationDao);
    }

    /**
     * Provides a singleton instance of ApplicationMessageService.
     */
    @Provides
    @Singleton
    public ApplicationMessageService provideApplicationMessageService(ApplicationMessageDao applicationMessageDao) {
        return new ApplicationMessageService(applicationMessageDao);
    }

    /**
     * Provides a singleton instance of CityService.
     */
    @Provides
    @Singleton
    public CityService provideCityService(CityDao cityDao) {
        return new CityService(cityDao);
    }

    /**
     * Provides a singleton instance of IndustryService.
     */
    @Provides
    @Singleton
    public IndustryService provideIndustryService(IndustryDao industryDao) {
        return new IndustryService(industryDao);
    }

    /**
     * Provides a singleton instance of ProfessionService.
     */
    @Provides
    @Singleton
    public ProfessionService provideProfessionService(ProfessionDao professionDao) {
        return new ProfessionService(professionDao);
    }

    /**
     * Provides a singleton instance of UserIndustryService.
     */
    @Provides
    @Singleton
    public UserIndustryService provideUserIndustryService(UserIndustryDao userIndustryDao) {
        return new UserIndustryService(userIndustryDao);
    }

    /**
     * Provides a singleton instance of UserProfessionService.
     */
    @Provides
    @Singleton
    public UserProfessionService provideUserProfessionService(UserProfessionDao userProfessionDao) {
        return new UserProfessionService(userProfessionDao);
    }

    /**
     * Provides a singleton instance of UserConnectionService.
     */
    @Provides
    @Singleton
    public UserConnectionService provideUserConnectionService(UserConnectionDao userConnectionDao) {
        return new UserConnectionService(userConnectionDao);
    }
}

