package com.company.tests.d4;

import static org.hamcrest.Matchers.*;

import io.qameta.allure.*;
import org.testng.annotations.Test;

import com.company.base.BaseTest;

/** ТК16. Восстановление папки */
@Epic("Yandex Disk")
@Feature("Управление папками")
@Story("Восстановление папки")
@Severity(SeverityLevel.CRITICAL)
public class RestoreFolderTest extends BaseTest {

    @Test(
            description = "ТК16. Восстановление папки",
            groups = {"yandex", "positive", "d4"})
    public void testRestoreFolder() {
        String folderPath = createTestFolderInTrash();

        ydApiClient
                .restoreFolderFromTrash(folderPath)
                .then()
                .statusCode(201)
                .body("href", containsString("TestFolder"));
    }
}
