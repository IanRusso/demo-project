package com.irusso.demoserver.db.dao;

import com.google.inject.Inject;
import com.irusso.demoserver.db.model.UserSkill;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * Data Access Object for the user_skills table.
 * Manages user skills with proficiency levels and years of experience.
 */
public class UserSkillDao extends StandardDao<UserSkill, Long> {

    private static final String TABLE_NAME = "user_skills";
    private static final String ID_COLUMN = "id";

    // Column name constants
    private static final String COL_USER_ID = "user_id";
    private static final String COL_SKILL_NAME = "skill_name";
    private static final String COL_PROFICIENCY_LEVEL = "proficiency_level";
    private static final String COL_YEARS_OF_EXPERIENCE = "years_of_experience";
    private static final String COL_CREATED_AT = "created_at";

    private static final RowMapper<UserSkill> USER_SKILL_MAPPER = (rs, ctx) -> {
        UserSkill skill = new UserSkill();
        skill.setId(rs.getLong(ID_COLUMN));
        skill.setUserId(rs.getLong(COL_USER_ID));
        skill.setSkillName(rs.getString(COL_SKILL_NAME));
        skill.setProficiencyLevel(rs.getString(COL_PROFICIENCY_LEVEL));
        skill.setYearsOfExperience((Integer) rs.getObject(COL_YEARS_OF_EXPERIENCE));
        skill.setCreatedAt(rs.getTimestamp(COL_CREATED_AT));
        return skill;
    };
    
    /**
     * Constructor for UserSkillDao.
     * 
     * @param jdbi The JDBI instance for database access
     */
    @Inject
    public UserSkillDao(Jdbi jdbi) {
        super(jdbi,
            TableDefinition.<UserSkill>builder()
                .tableName(TABLE_NAME)
                .idColumn(ID_COLUMN)
                .addColumn(ColumnDefinition.<UserSkill>builder()
                    .columnName(COL_USER_ID)
                    .insertable(true)
                    .updatable(false)
                    .getter(UserSkill::getUserId)
                    .build())
                .addColumn(ColumnDefinition.<UserSkill>builder()
                    .columnName(COL_SKILL_NAME)
                    .insertable(true)
                    .updatable(true)
                    .getter(UserSkill::getSkillName)
                    .build())
                .addColumn(ColumnDefinition.<UserSkill>builder()
                    .columnName(COL_PROFICIENCY_LEVEL)
                    .insertable(true)
                    .updatable(true)
                    .getter(UserSkill::getProficiencyLevel)
                    .build())
                .addColumn(ColumnDefinition.<UserSkill>builder()
                    .columnName(COL_YEARS_OF_EXPERIENCE)
                    .insertable(true)
                    .updatable(true)
                    .getter(UserSkill::getYearsOfExperience)
                    .build())
                .addColumn(ColumnDefinition.<UserSkill>builder()
                    .columnName(COL_CREATED_AT)
                    .insertable(true)
                    .updatable(false)
                    .getter(s -> Timestamp.from(Instant.now()))
                    .build())
                .build(),
            USER_SKILL_MAPPER
        );
    }
    
    /**
     * Find all skills for a specific user.
     * 
     * @param userId The user ID
     * @return List of skills for the user
     */
    public List<UserSkill> findByUserId(Long userId) {
        String sql = "SELECT * FROM user_skills WHERE user_id = :userId ORDER BY skill_name";
        return executeQuery(sql, "userId", userId);
    }
    
    /**
     * Find users with a specific skill.
     * 
     * @param skillName The skill name to search for
     * @return List of user skills matching the skill name
     */
    public List<UserSkill> findBySkillName(String skillName) {
        String sql = "SELECT * FROM user_skills WHERE skill_name ILIKE :skillName ORDER BY years_of_experience DESC";
        return executeQuery(sql, "skillName", "%" + skillName + "%");
    }
    
    /**
     * Find users with a specific skill and minimum proficiency.
     * 
     * @param skillName The skill name
     * @param minYearsExperience Minimum years of experience
     * @return List of user skills matching criteria
     */
    public List<UserSkill> findBySkillAndExperience(String skillName, int minYearsExperience) {
        String sql = """
            SELECT * FROM user_skills 
            WHERE skill_name ILIKE :skillName 
            AND years_of_experience >= :minYears
            ORDER BY years_of_experience DESC
            """;
        return executeQuery(sql, 
            "skillName", "%" + skillName + "%",
            "minYears", minYearsExperience
        );
    }
    
    /**
     * Delete all skills for a specific user.
     * 
     * @param userId The user ID
     * @return Number of skills deleted
     */
    public int deleteByUserId(Long userId) {
        String sql = "DELETE FROM user_skills WHERE user_id = :userId";
        return executeUpdate(sql, "userId", userId);
    }
}

