package com.company.db;

/** SQL-запросы */
public final class SqlQueries {
    private SqlQueries() {}

    public static final String SELECT_POST_BY_ID =
            "SELECT post_title, post_content, post_status FROM wp_posts WHERE ID = ?";
    public static final String DELETE_POST_AND_REVISION =
            "DELETE FROM wp_posts WHERE ID = ? OR post_parent = ?";
    public static final String SELECT_COUNT_POSTS_BY_ID =
            "SELECT COUNT(*) AS cnt FROM wp_posts WHERE ID = ?";
    public static final String CREATE_TEST_POST =
            """
                INSERT INTO wp_posts
                (post_title, post_content, post_status, post_date, post_modified, post_date_gmt, post_modified_gmt,
                post_excerpt, to_ping, pinged, post_content_filtered)
                VALUES (?, ?, ?, NOW(), NOW(), NOW(), NOW(), '', '', '', '')
            """;
}
