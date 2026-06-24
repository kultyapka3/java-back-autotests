package com.company.base;

import java.util.ArrayList;
import java.util.List;

import io.qameta.allure.testng.AllureTestNg;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Listeners;

import com.company.config.Config;
import com.company.clients.DbClient;
import com.company.clients.WpApiClient;
import com.company.clients.YandexDiskApiClient;

/** Базовый класс для всех тестов */
@Listeners(AllureTestNg.class)
public class BaseTest {

    protected WpApiClient wpApiClient;
    protected DbClient dbClient;
    protected YandexDiskApiClient ydApiClient;

    /** Список ID постов для очистки */
    protected final ThreadLocal<List<Integer>> postsToCleanup =
            ThreadLocal.withInitial(ArrayList::new);

    /** Список ID комментариев для очистки */
    protected final ThreadLocal<List<Integer>> commentsToCleanup =
            ThreadLocal.withInitial(ArrayList::new);

    @BeforeMethod
    public void setUp() {
        wpApiClient = new WpApiClient();
        dbClient = new DbClient();
        ydApiClient = new YandexDiskApiClient();
        postsToCleanup.get().clear();
        commentsToCleanup.get().clear();
    }

    /** Создание тестового поста */
    protected int createTestPost() {
        int postId = dbClient.createTestPost("Test Title", "Test Content", "publish");
        postsToCleanup.get().add(postId);

        return postId;
    }

    /** Создание тестового комментария */
    protected int createTestComment() {
        int postId = this.createTestPost();
        int commentId =
                dbClient.createTestComment(
                        postId,
                        Config.getWpUser(),
                        Config.getWpEmail(),
                        "Test comment content",
                        "1");
        postsToCleanup.get().add(postId);
        commentsToCleanup.get().add(commentId);

        return commentId;
    }

    @AfterMethod
    public void tearDown() {
        for (Integer postId : postsToCleanup.get()) {
            try {
                dbClient.deletePostById(postId);
            } catch (Exception e) {
                System.err.println(
                        "Не удалось удалить пост с ID = " + postId + ". Ошибка: " + e.getMessage());
            }
        }

        for (Integer commentId : commentsToCleanup.get()) {
            try {
                dbClient.deleteCommentById(commentId);
            } catch (Exception e) {
                System.err.println(
                        "Не удалось удалить комментарий с ID = "
                                + commentId
                                + ". Ошибка: "
                                + e.getMessage());
            }
        }

        postsToCleanup.remove();
        commentsToCleanup.remove();
    }
}
