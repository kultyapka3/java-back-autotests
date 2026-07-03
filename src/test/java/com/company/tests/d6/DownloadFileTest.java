package com.company.tests.d6;

import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;

import io.qameta.allure.*;
import org.testng.annotations.Test;

import com.company.base.BaseTest;
import com.company.models.yandex.*;

/** ТК4. Скачивание текстового файла */
@Epic("Yandex Disk")
@Feature("Управление файлами")
@Story("Скачивание текстового файла")
@Severity(SeverityLevel.CRITICAL)
public class DownloadFileTest extends BaseTest {

    @Test(
            description = "ТК4. Скачивание текстового файла",
            groups = {"yandex", "positive", "d6"})
    public void testDownloadFile() {
        String filePath = uploadedFilePath();
        FileDataModel expectedFile = createTestFile();

        DownloadLinkResponse downloadLink =
                ydApiClient
                        .getDownloadLink(filePath)
                        .then()
                        .statusCode(200)
                        .body("href", notNullValue())
                        .extract()
                        .as(DownloadLinkResponse.class);

        String downloadedFileContent =
                ydApiClient
                        .downloadFileByLink(downloadLink.getHref())
                        .then()
                        .statusCode(200)
                        .extract()
                        .asString();

        Allure.step(
                "Сравниваем скачанный файл с исходным",
                () -> {
                    assertEquals(
                            downloadedFileContent,
                            new String(expectedFile.getContent()),
                            "Содержимое скачанного файла не совпадает с исходным");
                });
    }
}
