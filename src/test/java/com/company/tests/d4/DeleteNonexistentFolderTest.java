package com.company.tests.d4;

import static org.hamcrest.Matchers.*;

import io.qameta.allure.*;
import org.testng.annotations.Test;

import com.company.base.BaseTest;

/** ТК18. Удаление несуществующей папки */
@Epic("Yandex Disk")
@Feature("Управление папками")
@Story("Удаление несуществующей папки")
@Severity(SeverityLevel.NORMAL)
public class DeleteNonexistentFolderTest extends BaseTest {

    @Test(
            description = "ТК18. Удаление несуществующей папки",
            groups = {"yandex", "negative", "d4"})
    public void testDeleteNonexistentFolder() {
        String nonexistentFolderName = "123NONEXISTENT_FOLDER321";

        ydApiClient
                .deleteFolderPermanently(nonexistentFolderName)
                .then()
                .statusCode(404)
                .body("error", notNullValue())
                .body("message", notNullValue());
    }
}
