package com.company.tests.d4;

import static org.hamcrest.Matchers.*;

import io.qameta.allure.*;
import org.testng.annotations.Test;

import com.company.base.BaseTest;

/** ТК15. Удаление папки (перемещение в корзину) */
@Epic("Yandex Disk")
@Feature("Управление папками")
@Story("Удаление папки (перемещение в корзину)")
@Severity(SeverityLevel.CRITICAL)
public class DeleteFolderToTrashTest extends BaseTest {

    @Test(
            description = "ТК15. Удаление папки (перемещение в корзину)",
            groups = {"yandex", "positive", "d4"})
    public void testDeleteFolderToTrash() {
        String folderName = createTestFolder();

        ydApiClient
                .deleteFolderToTrash(folderName)
                .then()
                .statusCode(204)
                .body(isEmptyOrNullString());
    }
}
