DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'notifications'
          AND column_name = 'id'
          AND data_type = 'uuid'
    ) THEN
        ALTER TABLE notifications RENAME TO notifications_old;

        CREATE TABLE notifications (
            id BIGSERIAL PRIMARY KEY,
            user_id UUID NOT NULL,
            title VARCHAR(255) NOT NULL,
            description VARCHAR(500) NOT NULL,
            is_unread BOOLEAN NOT NULL DEFAULT TRUE,
            created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
            CONSTRAINT fk_notifications_user FOREIGN KEY (user_id) REFERENCES users(id)
        );

        INSERT INTO notifications (user_id, title, description, is_unread, created_at)
        SELECT user_id, title, description, is_unread, created_at
        FROM notifications_old;

        DROP TABLE notifications_old;

        CREATE INDEX idx_notifications_user_created_at ON notifications (user_id, created_at DESC);
    END IF;
END $$;
