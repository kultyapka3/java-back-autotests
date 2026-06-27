package com.company.tests.d4;

import static org.hamcrest.Matchers.*;

import io.qameta.allure.*;
import org.testng.annotations.Test;

import com.company.base.BaseTest;

/** ТК17. Создание уже существующей папки */
@Epic("Yandex Disk")
@Feature("Управление папками")
@Story("Создание уже существующей папки")
@Severity(SeverityLevel.CRITICAL)
public class CreateExistingFolderTest extends BaseTest {

    @Test(
            description = "ТК17. Создание уже существующей папки",
            groups = {"yandex", "negative", "d4"})
    public void testCreateExistingFolder() {
        String folderName = createTestFolder();

        ydApiClient
                .createFolder(folderName)
                .then()
                .statusCode(409)
                .body("error", notNullValue())
                .body("message", notNullValue());
    }
}
