package com.company.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.qameta.allure.testng.AllureTestNg;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Listeners;

import com.company.config.Config;
import com.company.clients.DbClient;
import com.company.clients.WpApiClient;
import com.company.clients.YandexDiskApiClient;
import com.company.models.yandex.TrashResourcesResponse;

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

    /** Список названий папок для очистки */
    protected final ThreadLocal<List<String>> foldersToCleanup =
            ThreadLocal.withInitial(ArrayList::new);

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        wpApiClient = new WpApiClient();
        dbClient = new DbClient();
        ydApiClient = new YandexDiskApiClient();
        postsToCleanup.get().clear();
        commentsToCleanup.get().clear();
        foldersToCleanup.get().clear();
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

    /** Создание тестовой папки */
    protected String createTestFolder() {
        String folderName = "TestFolder";
        ydApiClient.createFolder(folderName);
        foldersToCleanup.get().add(folderName);

        return folderName;
    }

    /** Создание тестовой папки в корзине */
    protected String createTestFolderInTrash() {
        String folderName = "TestFolder";
        ydApiClient.createFolder(folderName);
        foldersToCleanup.get().add(folderName);
        ydApiClient.deleteFolderToTrash(folderName);

        TrashResourcesResponse response =
                ydApiClient.getTrashItems().then().extract().as(TrashResourcesResponse.class);

        Optional<TrashResourcesResponse.Embedded.Items> targetItem =
                response.get_embedded().getItems().stream()
                        .filter(item -> item.getPath().contains(folderName))
                        .findFirst();

        return targetItem.get().getPath();
    }

    @AfterMethod(alwaysRun = true)
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

        for (String dirPath : foldersToCleanup.get()) {
            int statusCode =
                    ydApiClient.deleteFolderPermanently(dirPath).then().extract().statusCode();

            if (statusCode == 404) {
                try {
                    TrashResourcesResponse response =
                            ydApiClient
                                    .getTrashItems()
                                    .then()
                                    .extract()
                                    .as(TrashResourcesResponse.class);

                    Optional<TrashResourcesResponse.Embedded.Items> targetItem =
                            response.get_embedded().getItems().stream()
                                    .filter(item -> item.getPath().contains(dirPath))
                                    .findFirst();

                    if (targetItem.isPresent()) {
                        ydApiClient.restoreFolderFromTrash(targetItem.get().getPath());
                        ydApiClient.deleteFolderPermanently(dirPath);
                    }
                } catch (Exception e) {
                    System.err.println(
                            "Не удалось удалить папку с названием = "
                                    + dirPath
                                    + ". Ошибка: "
                                    + e.getMessage());
                }
            }
        }

        postsToCleanup.remove();
        commentsToCleanup.remove();
        foldersToCleanup.remove();
    }
}
