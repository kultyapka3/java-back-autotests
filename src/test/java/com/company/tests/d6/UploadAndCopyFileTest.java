package com.company.tests.d6;

import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;

import java.util.List;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import com.company.base.BaseTest;
import com.company.models.yandex.FileDataModel;
import com.company.models.yandex.UploadLinkResponse;
import com.company.models.yandex.UploadFileRequest;
import com.company.models.yandex.CopyFileRequest;
import com.company.models.yandex.ResourcesResponse;

/** ТК3. Загрузка и копирование файла */
@Epic("Yandex Disk")
@Feature("Управление файлами")
@Story("Загрузка и копирование файла")
@Severity(SeverityLevel.CRITICAL)
public class UploadAndCopyFileTest extends BaseTest {

    @Test(
            description = "ТК3. Загрузка и копирование файла",
            groups = {"yandex", "negative", "d6"})
    public void testUploadAndCopyFile() {
        List<String> paths = createInputAndOutputFolders();
        String inputPath = paths.getFirst();
        String outputPath = paths.getLast();

        FileDataModel testFile = createTestFile();
        String sourcePath = inputPath + "/" + testFile.getName();
        String destinationPath = outputPath + "/" + testFile.getName();

        UploadLinkResponse uploadLinkResponse =
                ydApiClient
                        .getUploadLink(sourcePath)
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

        CopyFileRequest copyRequest =
                CopyFileRequest.builder().from(sourcePath).path(destinationPath).build();

        ydApiClient
                .copyFile(copyRequest)
                .then()
                .statusCode(201)
                .body("method", notNullValue())
                .body("href", notNullValue());

        ResourcesResponse copiedFile =
                ydApiClient
                        .getItems(destinationPath)
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(ResourcesResponse.class);

        Allure.step(
                "Проверка наличия файла в папке назначения",
                () -> {
                    assertEquals(copiedFile.getName(), testFile.getName());
                    assertEquals(copiedFile.getMime_type(), "text/plain");
                    assertEquals(copiedFile.getMedia_type(), "document");
                });

        ydApiClient
                .copyFile(copyRequest)
                .then()
                .statusCode(409)
                .body("error", notNullValue())
                .body("description", notNullValue())
                .body("message", notNullValue());
    }
}
