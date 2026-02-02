package com.irusso.demoserver.db.dao;

import com.google.inject.Inject;
import com.irusso.demoserver.db.model.UserConnection;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * Data Access Object for the user_connections table.
 */
public class UserConnectionDao extends StandardDao<UserConnection, Long> {

    private static final String TABLE_NAME = "user_connections";
    private static final String ID_COLUMN = "id";

    // Column name constants
    private static final String COL_USER_ID = "user_id";
    private static final String COL_CONNECTED_USER_ID = "connected_user_id";
    private static final String COL_CONNECTION_STATUS = "connection_status";
    private static final String COL_CONNECTION_TYPE = "connection_type";
    private static final String COL_INITIATED_BY_USER_ID = "initiated_by_user_id";
    private static final String COL_CONNECTED_AT = "connected_at";
    private static final String COL_CREATED_AT = "created_at";
    private static final String COL_UPDATED_AT = "updated_at";

    private static final RowMapper<UserConnection> MAPPER = (rs, ctx) -> {
        UserConnection connection = new UserConnection();
        connection.setId(rs.getLong(ID_COLUMN));
        connection.setUserId(rs.getLong(COL_USER_ID));
        connection.setConnectedUserId(rs.getLong(COL_CONNECTED_USER_ID));
        connection.setConnectionStatus(rs.getString(COL_CONNECTION_STATUS));
        
        String connectionType = rs.getString(COL_CONNECTION_TYPE);
        if (!rs.wasNull()) {
            connection.setConnectionType(connectionType);
        }
        
        connection.setInitiatedByUserId(rs.getLong(COL_INITIATED_BY_USER_ID));
        
        Timestamp connectedAt = rs.getTimestamp(COL_CONNECTED_AT);
        if (!rs.wasNull()) {
            connection.setConnectedAt(connectedAt);
        }
        
        connection.setCreatedAt(rs.getTimestamp(COL_CREATED_AT));
        connection.setUpdatedAt(rs.getTimestamp(COL_UPDATED_AT));
        return connection;
    };

    @Inject
    public UserConnectionDao(Jdbi jdbi) {
        super(jdbi,
            TableDefinition.<UserConnection>builder()
                .tableName(TABLE_NAME)
                .idColumn(ID_COLUMN)
                .addColumn(ColumnDefinition.<UserConnection>builder()
                    .columnName(COL_USER_ID)
                    .javaType(Long.class)
                    .insertable(true)
                    .updatable(false)
                    .getter(UserConnection::getUserId)
                    .build())
                .addColumn(ColumnDefinition.<UserConnection>builder()
                    .columnName(COL_CONNECTED_USER_ID)
                    .javaType(Long.class)
                    .insertable(true)
                    .updatable(false)
                    .getter(UserConnection::getConnectedUserId)
                    .build())
                .addColumn(ColumnDefinition.<UserConnection>builder()
                    .columnName(COL_CONNECTION_STATUS)
                    .javaType(String.class)
                    .insertable(true)
                    .updatable(true)
                    .getter(UserConnection::getConnectionStatus)
                    .build())
                .addColumn(ColumnDefinition.<UserConnection>builder()
                    .columnName(COL_CONNECTION_TYPE)
                    .javaType(String.class)
                    .insertable(true)
                    .updatable(true)
                    .getter(UserConnection::getConnectionType)
                    .build())
                .addColumn(ColumnDefinition.<UserConnection>builder()
                    .columnName(COL_INITIATED_BY_USER_ID)
                    .javaType(Long.class)
                    .insertable(true)
                    .updatable(false)
                    .getter(UserConnection::getInitiatedByUserId)
                    .build())
                .addColumn(ColumnDefinition.<UserConnection>builder()
                    .columnName(COL_CONNECTED_AT)
                    .javaType(Timestamp.class)
                    .insertable(true)
                    .updatable(true)
                    .getter(UserConnection::getConnectedAt)
                    .build())
                .addColumn(ColumnDefinition.<UserConnection>builder()
                    .columnName(COL_CREATED_AT)
                    .javaType(Timestamp.class)
                    .insertable(true)
                    .updatable(false)
                    .getter(c -> Timestamp.from(Instant.now()))
                    .build())
                .addColumn(ColumnDefinition.<UserConnection>builder()
                    .columnName(COL_UPDATED_AT)
                    .javaType(Timestamp.class)
                    .insertable(true)
                    .updatable(true)
                    .getter(c -> Timestamp.from(Instant.now()))
                    .build())
                .build(),
            MAPPER
        );
    }
    
    /**
     * Find all connections for a specific user (both as user_id and connected_user_id).
     */
    public List<UserConnection> findByUserId(Long userId) {
        String sql = "SELECT * FROM user_connections WHERE user_id = :userId OR connected_user_id = :userId ORDER BY created_at DESC";
        return executeQuery(sql, "userId", userId);
    }
    
    /**
     * Find all accepted connections for a specific user.
     */
    public List<UserConnection> findAcceptedByUserId(Long userId) {
        String sql = "SELECT * FROM user_connections WHERE (user_id = :userId OR connected_user_id = :userId) AND connection_status = 'ACCEPTED' ORDER BY connected_at DESC";
        return executeQuery(sql, "userId", userId);
    }
    
    /**
     * Find all pending connection requests for a specific user.
     */
    public List<UserConnection> findPendingByUserId(Long userId) {
        String sql = "SELECT * FROM user_connections WHERE (user_id = :userId OR connected_user_id = :userId) AND connection_status = 'PENDING' ORDER BY created_at DESC";
        return executeQuery(sql, "userId", userId);
    }
    
    /**
     * Find pending connection requests received by a user (where they need to respond).
     */
    public List<UserConnection> findPendingReceivedByUserId(Long userId) {
        String sql = "SELECT * FROM user_connections WHERE connected_user_id = :userId AND connection_status = 'PENDING' AND initiated_by_user_id != :userId ORDER BY created_at DESC";
        return executeQuery(sql, "userId", userId);
    }
    
    /**
     * Find pending connection requests sent by a user (where they initiated).
     */
    public List<UserConnection> findPendingSentByUserId(Long userId) {
        String sql = "SELECT * FROM user_connections WHERE user_id = :userId AND connection_status = 'PENDING' AND initiated_by_user_id = :userId ORDER BY created_at DESC";
        return executeQuery(sql, "userId", userId);
    }
    
    /**
     * Find a specific connection between two users (bidirectional).
     */
    public List<UserConnection> findConnectionBetweenUsers(Long userId1, Long userId2) {
        String sql = "SELECT * FROM user_connections WHERE (user_id = :userId1 AND connected_user_id = :userId2) OR (user_id = :userId2 AND connected_user_id = :userId1)";
        return jdbi.withHandle(handle ->
            handle.createQuery(sql)
                .bind("userId1", userId1)
                .bind("userId2", userId2)
                .map(MAPPER)
                .list()
        );
    }
    
    /**
     * Delete all connections for a specific user.
     */
    public int deleteByUserId(Long userId) {
        String sql = "DELETE FROM user_connections WHERE user_id = :userId OR connected_user_id = :userId";
        return executeUpdate(sql, "userId", userId);
    }
}

