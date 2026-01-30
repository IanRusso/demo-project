-- Add industries and professions tables for job categorization
-- These tables can be populated via import APIs from CSV files

-- ============================================
-- INDUSTRIES
-- ============================================

CREATE TABLE industries (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_industries_name ON industries(name);

COMMENT ON TABLE industries IS 'Industry categories for job postings and user interests';
COMMENT ON COLUMN industries.name IS 'Industry name (e.g., "Technology", "Healthcare")';

-- ============================================
-- PROFESSIONS
-- ============================================

CREATE TABLE professions (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    prep SMALLINT NOT NULL CHECK (prep >= 1 AND prep <= 5),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_professions_name ON professions(name);
CREATE INDEX idx_professions_prep ON professions(prep);

COMMENT ON TABLE professions IS 'Profession/occupation categories with preparation level requirements';
COMMENT ON COLUMN professions.name IS 'Profession name (e.g., "Software Engineer", "Registered Nurse")';
COMMENT ON COLUMN professions.prep IS 'Preparation level: 1=Little/No Prep, 2=Some Prep, 3=Medium Prep, 4=Considerable Prep, 5=Extensive Prep';

