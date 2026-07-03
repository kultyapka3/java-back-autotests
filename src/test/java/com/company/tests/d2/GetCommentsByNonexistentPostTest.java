package com.company.tests.d2;

import static org.hamcrest.Matchers.hasSize;

import io.qameta.allure.*;
import org.testng.annotations.Test;

import com.company.base.BaseTest;

/** ТК13. Получение комментариев несуществующего поста */
@Epic("WordPress DB")
@Feature("Получение данных")
@Story("Получение комментариев несуществующего поста")
@Severity(SeverityLevel.CRITICAL)
public class GetCommentsByNonexistentPostTest extends BaseTest {

    @Test(
            description = "ТК13. Получение комментариев несуществующего поста",
            groups = {"wp", "negative", "comments", "d2"})
    public void testGetCommentsByNonexistentPost() {
        int nonExistentPostID = 9999;

        wpApiClient
                .getCommentsByPostId(nonExistentPostID)
                .then()
                .statusCode(200)
                .body("$", hasSize(0));
    }
}
