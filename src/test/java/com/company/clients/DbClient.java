package com.company.clients;

import java.util.Optional;
import java.sql.*;

import io.qameta.allure.Step;

import com.company.config.Config;
import com.company.db.SqlQueries;
import com.company.db.CommentRecord;
import com.company.db.PostRecord;

/** Клиент для работы с БД WordPress */
public class DbClient {

    private final String url;
    private final String user;
    private final String pass;

    public DbClient() {
        this.url = Config.getDbUrl();
        this.user = Config.getDbUser();
        this.pass = Config.getDbPass();
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, pass);
    }

    // Posts
    /** Получает пост по ID */
    @Step("Получение поста по ID = {postId}")
    public Optional<PostRecord> getPostById(int postId) {
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SqlQueries.SELECT_POST_BY_ID)) {

            stmt.setInt(1, postId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    PostRecord record =
                            PostRecord.builder()
                                    .postTitle(rs.getString("post_title"))
                                    .postContent(rs.getString("post_content"))
                                    .postStatus(rs.getString("post_status"))
                                    .build();

                    return Optional.of(record);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка DB: getPostById", e);
        }
        return Optional.empty();
    }

    /** Получает количество постов по ID */
    @Step("Получение количества постов по ID = {postId}")
    public int getCountPostsById(int postId) {
        try (Connection conn = getConnection();
                PreparedStatement stmt =
                        conn.prepareStatement(SqlQueries.SELECT_COUNT_POSTS_BY_ID)) {
            stmt.setInt(1, postId);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt("cnt") : 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка DB: getCountPostsById", e);
        }
    }

    /** Удаляет пост и его ревизию по ID */
    @Step("Удаление поста и его ревизии с ID = {postId}")
    public void deletePostById(int postId) {
        try (Connection conn = getConnection();
                PreparedStatement stmt =
                        conn.prepareStatement(SqlQueries.DELETE_POST_AND_REVISION)) {
            stmt.setInt(1, postId);
            stmt.setInt(2, postId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка DB: deletePostById", e);
        }
    }

    /** Создает тестовый пост */
    @Step("Создание тестового поста с title = {title}, content = {content}, status = {status}")
    public int createTestPost(String title, String content, String status) {
        try (Connection conn = getConnection();
                PreparedStatement stmt =
                        conn.prepareStatement(
                                SqlQueries.CREATE_TEST_POST, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, title);
            stmt.setString(2, content);
            stmt.setString(3, status);
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка DB: createTestPost", e);
        }
    }

    // Comments
    /** Получает комментарий по ID */
    @Step("Получение комментария по ID = {commentId}")
    public Optional<CommentRecord> getCommentById(int commentId) {
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SqlQueries.SELECT_COMMENT_BY_ID)) {
            stmt.setInt(1, commentId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    CommentRecord record =
                            CommentRecord.builder()
                                    .commentId(rs.getInt("comment_ID"))
                                    .commentPostId(rs.getInt("comment_post_ID"))
                                    .commentAuthor(rs.getString("comment_author"))
                                    .commentAuthorEmail(rs.getString("comment_author_email"))
                                    .commentContent(rs.getString("comment_content"))
                                    .commentApproved(rs.getString("comment_approved"))
                                    .build();

                    return Optional.of(record);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка DB: getCommentById", e);
        }
        return Optional.empty();
    }

    /** Получает количество комментариев по ID */
    @Step("Получение количества комментариев по ID = {commentId}")
    public int getCountCommentsById(int commentId) {
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SqlQueries.COUNT_COMMENTS_BY_ID)) {
            stmt.setInt(1, commentId);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt("cnt") : 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка DB: getCommentCountById", e);
        }
    }

    /** Удаляет комментарий по ID */
    @Step("Удаление комментария по ID = {commentId}")
    public void deleteCommentById(int commentId) {
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SqlQueries.DELETE_COMMENT_BY_ID)) {
            stmt.setInt(1, commentId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка DB: deleteCommentById", e);
        }
    }

    /** Создает тестовый комментарий */
    @Step(
            "Создание тестового комментария к посту с ID = {postId}, author = {author}, email = {email}, "
                    + "content = {content}, approved = {approved}")
    public int createTestComment(
            int postId, String author, String email, String content, String approved) {
        try (Connection conn = getConnection();
                PreparedStatement stmt =
                        conn.prepareStatement(
                                SqlQueries.CREATE_TEST_COMMENT, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, postId);
            stmt.setString(2, author);
            stmt.setString(3, email);
            stmt.setString(4, content);
            stmt.setString(5, approved);
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка DB: createTestComment", e);
        }
    }
}
