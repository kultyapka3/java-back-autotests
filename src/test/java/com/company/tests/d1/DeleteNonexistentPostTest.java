package com.company.tests.d1;

import static org.hamcrest.Matchers.notNullValue;
import static org.testng.Assert.assertEquals;

import io.qameta.allure.*;
import org.testng.annotations.Test;

import com.company.base.BaseTest;

/** ТК06. Удаление несуществующего поста */
@Epic("WordPress DB")
@Feature("Управление постами")
@Story("Удаление несуществующего поста")
@Severity(SeverityLevel.NORMAL)
public class DeleteNonexistentPostTest extends BaseTest {

    @Test(
            description = "ТК06. Удаление несуществующего поста",
            groups = {"wp", "negative", "posts", "d1"})
    public void testDeleteNonexistentPost() {
        int nonexistentPostId = 9999;

        wpApiClient
                .deletePost(nonexistentPostId, true)
                .then()
                .statusCode(404)
                .body("code", notNullValue())
                .body("message", notNullValue());

        Allure.step(
                "Проверка отсутствия поста в БД",
                () -> {
                    int count = dbClient.getCountPostsById(nonexistentPostId);
                    assertEquals(count, 0, "Пост с ID = " + nonexistentPostId + " существует");
                });
    }
}
