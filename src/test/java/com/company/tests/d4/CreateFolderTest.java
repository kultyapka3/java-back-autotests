package com.company.tests.d4;

import static org.hamcrest.Matchers.*;

import io.qameta.allure.*;
import org.testng.annotations.Test;

import com.company.base.BaseTest;

/** ТК14. Создание папки */
@Epic("Yandex Disk")
@Feature("Управление папками")
@Story("Создание папки")
@Severity(SeverityLevel.CRITICAL)
public class CreateFolderTest extends BaseTest {

    @Test(
            description = "ТК14. Создание папки",
            groups = {"yandex", "positive", "d4"})
    public void testCreateFolder() {
        String folderName = "FolderForTest";

        ydApiClient
                .createFolder(folderName)
                .then()
                .statusCode(201)
                .body("href", containsString(folderName));

        foldersToCleanup.get().add(folderName);
    }
}
