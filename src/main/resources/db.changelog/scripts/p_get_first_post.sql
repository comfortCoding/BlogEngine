CREATE PROCEDURE p_get_first_post(
    IN now_server_date DATE,
    OUT time DATE
)
BEGIN
SET time = (
		SELECT
			p.time
		FROM posts p
		WHERE p.is_active = 1
		AND p.moderation_status = 'ACCEPTED'
		AND p.time <= now_server_date
		ORDER BY p.time ASC
        LIMIT 1);
END //