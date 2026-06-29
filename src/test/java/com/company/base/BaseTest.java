package com.company.base;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.company.models.yandex.ResourcesResponse;
import com.company.models.yandex.FileDataModel;
import com.company.models.yandex.UploadFileRequest;
import com.company.models.yandex.UploadLinkResponse;

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

        ResourcesResponse response =
                ydApiClient.getTrashItems().then().extract().as(ResourcesResponse.class);

        Optional<ResourcesResponse.Embedded.Items> targetItem =
                response.get_embedded().getItems().stream()
                        .filter(item -> item.getPath().contains(folderName))
                        .findFirst();

        return targetItem.get().getPath();
    }

    /** Генерация тестового файла */
    protected FileDataModel createTestFile() {
        String fileName = "data.txt";
        byte[] fileContent = "username=SDET\npassword=secret_key".getBytes();

        return new FileDataModel(fileName, fileContent);
    }

    /** Создание папок input_data и output_data */
    protected List<String> createInputAndOutputFolders() {
        String inputPath = "input_data";
        String outputPath = "output_data";

        ydApiClient.createFolder(inputPath);
        foldersToCleanup.get().add(inputPath);

        ydApiClient.createFolder(outputPath);
        foldersToCleanup.get().add(outputPath);

        return List.of(inputPath, outputPath);
    }

    /** Загрузка тестового файла в sdet_data */
    protected String uploadedFilePath() {
        String inputPath = "sdet_data";
        ydApiClient.createFolder(inputPath);
        foldersToCleanup.get().add(inputPath);

        FileDataModel testFile = createTestFile();
        String filePath = inputPath + "/" + testFile.getName();

        UploadLinkResponse uploadLinkResponse =
                ydApiClient
                        .getUploadLink(filePath)
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(UploadLinkResponse.class);

        UploadFileRequest uploadRequest =
                UploadFileRequest.builder()
                        .url(uploadLinkResponse.getHref())
                        .content(testFile.getContent())
                        .filename(testFile.getName())
                        .build();

        ydApiClient.uploadFileByLink(uploadRequest).then().statusCode(201);

        return filePath;
    }

    /** Генерация и загрузка трех тестовых файлов */
    protected String uploadedFilesPath() {
        String testFolder = createTestFolder();
        List<FileDataModel> filesToCreate =
                List.of(
                        FileDataModel.builder()
                                .name("testData1.txt")
                                .content("Test data 1".getBytes())
                                .build(),
                        FileDataModel.builder()
                                .name("testData2.txt")
                                .content("Test data 2".getBytes())
                                .build(),
                        FileDataModel.builder()
                                .name("testData3.txt")
                                .content("Test data 3".getBytes())
                                .build());

        for (var file : filesToCreate) {
            String filePath = testFolder + "/" + file.getName();

            UploadLinkResponse uploadLinkResponse =
                    ydApiClient
                            .getUploadLink(filePath)
                            .then()
                            .statusCode(200)
                            .extract()
                            .as(UploadLinkResponse.class);

            UploadFileRequest uploadRequest =
                    UploadFileRequest.builder()
                            .url(uploadLinkResponse.getHref())
                            .content(file.getContent())
                            .filename(file.getName())
                            .build();

            ydApiClient.uploadFileByLink(uploadRequest).then().statusCode(201);
        }

        return testFolder;
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
                    ResourcesResponse response =
                            ydApiClient
                                    .getTrashItems()
                                    .then()
                                    .extract()
                                    .as(ResourcesResponse.class);

                    Optional<ResourcesResponse.Embedded.Items> targetItem =
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
