package com.irusso.demoserver.db.dao;

import com.google.inject.Inject;
import com.irusso.demoserver.db.model.EmployerHistory;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * Data Access Object for the employer_history table.
 */
public class EmployerHistoryDao extends StandardDao<EmployerHistory, Long> {

    private static final String TABLE_NAME = "employer_history";
    private static final String ID_COLUMN = "id";

    // Column name constants
    private static final String COL_EMPLOYER_ID = "employer_id";
    private static final String COL_EVENT_TYPE = "event_type";
    private static final String COL_EVENT_DATE = "event_date";
    private static final String COL_NUMBER_OF_POSITIONS = "number_of_positions";
    private static final String COL_SOURCE = "source";
    private static final String COL_DESCRIPTION = "description";
    private static final String COL_CREATED_AT = "created_at";

    private static final RowMapper<EmployerHistory> MAPPER = (rs, ctx) -> {
        EmployerHistory history = new EmployerHistory();
        history.setId(rs.getLong(ID_COLUMN));
        history.setEmployerId(rs.getLong(COL_EMPLOYER_ID));
        history.setEventType(rs.getString(COL_EVENT_TYPE));
        history.setEventDate(rs.getDate(COL_EVENT_DATE));
        history.setNumberOfPositions((Integer) rs.getObject(COL_NUMBER_OF_POSITIONS));
        history.setSource(rs.getString(COL_SOURCE));
        history.setDescription(rs.getString(COL_DESCRIPTION));
        history.setCreatedAt(rs.getTimestamp(COL_CREATED_AT));
        return history;
    };
    
    @Inject
    public EmployerHistoryDao(Jdbi jdbi) {
        super(jdbi,
            TableDefinition.<EmployerHistory>builder()
                .tableName(TABLE_NAME)
                .idColumn(ID_COLUMN)
                .addColumn(ColumnDefinition.<EmployerHistory>builder()
                    .columnName(COL_EMPLOYER_ID)
                    .javaType(Long.class)
                    .insertable(true)
                    .updatable(false)
                    .getter(EmployerHistory::getEmployerId)
                    .build())
                .addColumn(ColumnDefinition.<EmployerHistory>builder()
                    .columnName(COL_EVENT_TYPE)
                    .javaType(String.class)
                    .insertable(true)
                    .updatable(true)
                    .getter(EmployerHistory::getEventType)
                    .build())
                .addColumn(ColumnDefinition.<EmployerHistory>builder()
                    .columnName(COL_EVENT_DATE)
                    .javaType(Date.class)
                    .insertable(true)
                    .updatable(true)
                    .getter(EmployerHistory::getEventDate)
                    .build())
                .addColumn(ColumnDefinition.<EmployerHistory>builder()
                    .columnName(COL_NUMBER_OF_POSITIONS)
                    .javaType(Integer.class)
                    .insertable(true)
                    .updatable(true)
                    .getter(EmployerHistory::getNumberOfPositions)
                    .build())
                .addColumn(ColumnDefinition.<EmployerHistory>builder()
                    .columnName(COL_SOURCE)
                    .javaType(String.class)
                    .insertable(true)
                    .updatable(true)
                    .getter(EmployerHistory::getSource)
                    .build())
                .addColumn(ColumnDefinition.<EmployerHistory>builder()
                    .columnName(COL_DESCRIPTION)
                    .javaType(String.class)
                    .insertable(true)
                    .updatable(true)
                    .getter(EmployerHistory::getDescription)
                    .build())
                .addColumn(ColumnDefinition.<EmployerHistory>builder()
                    .columnName(COL_CREATED_AT)
                    .javaType(Timestamp.class)
                    .insertable(true)
                    .updatable(false)
                    .getter(h -> Timestamp.from(Instant.now()))
                    .build())
                .build(),
            MAPPER
        );
    }
    
    public List<EmployerHistory> findByEmployerId(Long employerId) {
        String sql = "SELECT * FROM employer_history WHERE employer_id = :employerId ORDER BY event_date DESC";
        return executeQuery(sql, "employerId", employerId);
    }
    
    public List<EmployerHistory> findByEventType(Long employerId, String eventType) {
        String sql = "SELECT * FROM employer_history WHERE employer_id = :employerId AND event_type = :eventType ORDER BY event_date DESC";
        return executeQuery(sql, "employerId", employerId, "eventType", eventType);
    }
    
    public int deleteByEmployerId(Long employerId) {
        String sql = "DELETE FROM employer_history WHERE employer_id = :employerId";
        return executeUpdate(sql, "employerId", employerId);
    }
}

