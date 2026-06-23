package com.company.db;

/** SQL-запросы */
public final class SqlQueries {
    private SqlQueries() {}

    // Posts
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

    // Comments
    public static final String SELECT_COMMENT_BY_ID =
            """
                SELECT comment_ID, comment_post_ID, comment_author, comment_author_email, comment_content,
                comment_approved
                FROM wp_comments WHERE comment_ID = ?
            """;
    public static final String DELETE_COMMENT_BY_ID =
            "DELETE FROM wp_comments WHERE comment_ID = ?";
    public static final String COUNT_COMMENTS_BY_ID =
            "SELECT COUNT(*) AS cnt FROM wp_comments WHERE comment_ID = ?";
    public static final String CREATE_TEST_COMMENT =
            """
                INSERT INTO wp_comments (comment_post_ID, comment_author, comment_author_email,
                comment_content, comment_approved, comment_date, comment_date_gmt)
                VALUES (?, ?, ?, ?, ?, NOW(), NOW())
            """;
}
