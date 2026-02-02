-- ============================================
-- USER INDUSTRIES
-- ============================================
-- Junction table linking users to industries they're interested in or have experience with

CREATE TABLE user_industries (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    industry_id BIGINT NOT NULL REFERENCES industries(id) ON DELETE CASCADE,
    is_current BOOLEAN DEFAULT false,
    years_of_experience INTEGER,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, industry_id)
);

CREATE INDEX idx_user_industries_user_id ON user_industries(user_id);
CREATE INDEX idx_user_industries_industry_id ON user_industries(industry_id);
CREATE INDEX idx_user_industries_is_current ON user_industries(is_current);

-- ============================================
-- USER PROFESSIONS
-- ============================================
-- Junction table linking users to professions/occupations they're interested in or have experience with

CREATE TABLE user_professions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    profession_id BIGINT NOT NULL REFERENCES professions(id) ON DELETE CASCADE,
    is_current BOOLEAN DEFAULT false,
    years_of_experience INTEGER,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, profession_id)
);

CREATE INDEX idx_user_professions_user_id ON user_professions(user_id);
CREATE INDEX idx_user_professions_profession_id ON user_professions(profession_id);
CREATE INDEX idx_user_professions_is_current ON user_professions(is_current);

