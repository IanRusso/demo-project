package com.irusso.demoserver.db.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum representing the preparation level required for a profession.
 * Based on O*NET Job Zones classification.
 */
public enum PreparationLevel {
    LITTLE(1, "Little or No Preparation Needed"),
    SOME(2, "Some Preparation Needed"),
    MEDIUM(3, "Medium Preparation Needed"),
    CONSIDERABLE(4, "Considerable Preparation Needed"),
    EXTENSIVE(5, "Extensive Preparation Needed");

    private final int value;
    private final String description;

    PreparationLevel(int value, String description) {
        this.value = value;
        this.description = description;
    }

    @JsonValue
    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Get PreparationLevel from integer value.
     *
     * @param value The integer value (1-5)
     * @return The corresponding PreparationLevel
     * @throws IllegalArgumentException if value is not 1-5
     */
    @JsonCreator
    public static PreparationLevel fromValue(int value) {
        for (PreparationLevel level : PreparationLevel.values()) {
            if (level.value == value) {
                return level;
            }
        }
        throw new IllegalArgumentException("Invalid preparation level: " + value + ". Must be 1-5.");
    }

    /**
     * Get PreparationLevel from Double (for CSV parsing).
     *
     * @param value The double value
     * @return The corresponding PreparationLevel, or null if value is null
     * @throws IllegalArgumentException if value is not 1-5
     */
    public static PreparationLevel fromDouble(Double value) {
        if (value == null) {
            return null;
        }
        return fromValue(value.intValue());
    }

    @Override
    public String toString() {
        return value + ": " + description;
    }
}

