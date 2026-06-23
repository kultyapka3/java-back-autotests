package com.company.tests.d1;

import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;

import io.qameta.allure.*;
import org.testng.annotations.Test;

import com.company.base.BaseTest;
import com.company.models.wp.PostRequest;

/** ТК05. Обновление несуществующего поста */
@Epic("WordPress DB")
@Feature("Управление постами")
@Story("Обновление несуществующего поста")
@Severity(SeverityLevel.NORMAL)
public class UpdateNonexistentPostTest extends BaseTest {

    @Test(
            description = "ТК05. Обновление несуществующего поста",
            groups = {"wp", "negative", "posts", "d1"})
    public void testUpdateNonexistentPost() {
        int nonexistentPostId = 9999;

        PostRequest updateRequest = PostRequest.builder().title("New Title").build();

        wpApiClient
                .updatePost(nonexistentPostId, updateRequest)
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
