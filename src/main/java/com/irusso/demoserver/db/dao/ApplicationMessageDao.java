package com.irusso.demoserver.db.dao;

import com.google.inject.Inject;
import com.irusso.demoserver.db.model.ApplicationMessage;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * Data Access Object for the application_messages table.
 */
public class ApplicationMessageDao extends StandardDao<ApplicationMessage, Long> {

    private static final String TABLE_NAME = "application_messages";
    private static final String ID_COLUMN = "id";

    // Column name constants
    private static final String COL_APPLICATION_ID = "application_id";
    private static final String COL_SENDER_TYPE = "sender_type";
    private static final String COL_MESSAGE_TYPE = "message_type";
    private static final String COL_MESSAGE_TEXT = "message_text";
    private static final String COL_SENT_AT = "sent_at";
    private static final String COL_READ_AT = "read_at";

    private static final RowMapper<ApplicationMessage> MAPPER = (rs, ctx) -> {
        ApplicationMessage msg = new ApplicationMessage();
        msg.setId(rs.getLong(ID_COLUMN));
        msg.setApplicationId(rs.getLong(COL_APPLICATION_ID));
        msg.setSenderType(rs.getString(COL_SENDER_TYPE));
        msg.setMessageType(rs.getString(COL_MESSAGE_TYPE));
        msg.setMessageText(rs.getString(COL_MESSAGE_TEXT));
        msg.setSentAt(rs.getTimestamp(COL_SENT_AT));
        msg.setReadAt(rs.getTimestamp(COL_READ_AT));
        return msg;
    };

    @Inject
    public ApplicationMessageDao(Jdbi jdbi) {
        super(jdbi,
            TableDefinition.<ApplicationMessage>builder()
                .tableName(TABLE_NAME)
                .idColumn(ID_COLUMN)
                .addColumn(ColumnDefinition.<ApplicationMessage>builder()
                    .columnName(COL_APPLICATION_ID)
                    .javaType(Long.class)
                    .insertable(true)
                    .updatable(false)
                    .getter(ApplicationMessage::getApplicationId)
                    .build())
                .addColumn(ColumnDefinition.<ApplicationMessage>builder()
                    .columnName(COL_SENDER_TYPE)
                    .javaType(String.class)
                    .insertable(true)
                    .updatable(false)
                    .getter(ApplicationMessage::getSenderType)
                    .build())
                .addColumn(ColumnDefinition.<ApplicationMessage>builder()
                    .columnName(COL_MESSAGE_TYPE)
                    .javaType(String.class)
                    .insertable(true)
                    .updatable(false)
                    .getter(ApplicationMessage::getMessageType)
                    .build())
                .addColumn(ColumnDefinition.<ApplicationMessage>builder()
                    .columnName(COL_MESSAGE_TEXT)
                    .javaType(String.class)
                    .insertable(true)
                    .updatable(true)
                    .getter(ApplicationMessage::getMessageText)
                    .build())
                .addColumn(ColumnDefinition.<ApplicationMessage>builder()
                    .columnName(COL_SENT_AT)
                    .javaType(Timestamp.class)
                    .insertable(true)
                    .updatable(false)
                    .getter(m -> Timestamp.from(Instant.now()))
                    .build())
                .addColumn(ColumnDefinition.<ApplicationMessage>builder()
                    .columnName(COL_READ_AT)
                    .javaType(Timestamp.class)
                    .insertable(true)
                    .updatable(true)
                    .getter(ApplicationMessage::getReadAt)
                    .build())
                .build(),
            MAPPER
        );
    }
    
    public List<ApplicationMessage> findByApplicationId(Long applicationId) {
        String sql = "SELECT * FROM application_messages WHERE application_id = :applicationId ORDER BY sent_at ASC";
        return executeQuery(sql, "applicationId", applicationId);
    }
    
    public List<ApplicationMessage> findUnreadByApplication(Long applicationId) {
        String sql = "SELECT * FROM application_messages WHERE application_id = :applicationId AND read_at IS NULL ORDER BY sent_at ASC";
        return executeQuery(sql, "applicationId", applicationId);
    }
    
    public List<ApplicationMessage> findBySenderType(Long applicationId, String senderType) {
        String sql = "SELECT * FROM application_messages WHERE application_id = :applicationId AND sender_type = :senderType ORDER BY sent_at ASC";
        return executeQuery(sql, "applicationId", applicationId, "senderType", senderType);
    }
    
    public boolean markAsRead(Long id) {
        String sql = "UPDATE application_messages SET read_at = :readAt WHERE id = :id AND read_at IS NULL";
        int rowsAffected = executeUpdate(sql, "id", id, "readAt", Timestamp.from(Instant.now()));
        return rowsAffected > 0;
    }
    
    public int markAllAsRead(Long applicationId, String senderType) {
        String sql = "UPDATE application_messages SET read_at = :readAt WHERE application_id = :applicationId AND sender_type = :senderType AND read_at IS NULL";
        return executeUpdate(sql, 
            "applicationId", applicationId, 
            "senderType", senderType,
            "readAt", Timestamp.from(Instant.now())
        );
    }
}

