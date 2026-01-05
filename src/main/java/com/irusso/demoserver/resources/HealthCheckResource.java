package com.irusso.demoserver.resources;

import com.codahale.metrics.health.HealthCheck;

/**
 * Health check for the Demo Server application.
 */
public class HealthCheckResource extends HealthCheck {

    @Override
    protected Result check() {
        // Add any health check logic here
        // For now, always return healthy
        return Result.healthy();
    }
}

