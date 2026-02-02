-- ============================================
-- USER CONNECTIONS
-- ============================================
-- Junction table for user-to-user connections (networking/professional relationships)
-- This is a self-referencing many-to-many relationship

CREATE TABLE user_connections (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    connected_user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    connection_status VARCHAR(50) NOT NULL DEFAULT 'PENDING', -- 'PENDING', 'ACCEPTED', 'BLOCKED'
    connection_type VARCHAR(50), -- 'COLLEAGUE', 'MENTOR', 'MENTEE', 'FRIEND', 'PROFESSIONAL', etc.
    initiated_by_user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    connected_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, connected_user_id),
    CONSTRAINT chk_connection_status CHECK (connection_status IN ('PENDING', 'ACCEPTED', 'BLOCKED')),
    CONSTRAINT chk_not_self_connection CHECK (user_id != connected_user_id)
);

CREATE INDEX idx_user_connections_user_id ON user_connections(user_id);
CREATE INDEX idx_user_connections_connected_user_id ON user_connections(connected_user_id);
CREATE INDEX idx_user_connections_status ON user_connections(connection_status);
CREATE INDEX idx_user_connections_initiated_by ON user_connections(initiated_by_user_id);

COMMENT ON TABLE user_connections IS 'User-to-user professional connections and networking relationships';
COMMENT ON COLUMN user_connections.user_id IS 'The user who owns this connection';
COMMENT ON COLUMN user_connections.connected_user_id IS 'The user they are connected to';
COMMENT ON COLUMN user_connections.connection_status IS 'Status of the connection: PENDING (awaiting acceptance), ACCEPTED (active connection), BLOCKED (blocked by one party)';
COMMENT ON COLUMN user_connections.connection_type IS 'Type of professional relationship';
COMMENT ON COLUMN user_connections.initiated_by_user_id IS 'The user who initiated the connection request';
COMMENT ON COLUMN user_connections.connected_at IS 'Timestamp when the connection was accepted (null if still pending)';

