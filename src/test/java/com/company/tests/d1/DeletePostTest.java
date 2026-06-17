package com.company.tests.d1;

import static org.testng.Assert.assertEquals;

import io.restassured.response.Response;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import com.company.base.BaseTest;

/** ТК03. Удаление поста */
@Epic("WordPress DB")
@Feature("Управление постами")
@Story("Удаление поста")
@Severity(SeverityLevel.CRITICAL)
public class DeletePostTest extends BaseTest {

    @Test(
            description = "ТК03. Удаление поста",
            groups = {"wp", "positive", "posts", "d1"})
    public void testUpdatePost() {
        int postId = createTestPost();

        Response apiResponse =
                Allure.step("Удаление поста", () -> wpApiClient.deletePost(postId, true));

        int statusCode = apiResponse.getStatusCode();

        Allure.step(
                "Проверка статуса ответа",
                () -> assertEquals(statusCode, 200, "Ожидался 200 OK, получен " + statusCode));

        Allure.step(
                "Проверка, что пост был удален",
                () -> {
                    int count = dbClient.getCountPostsById(postId);
                    assertEquals(count, 0, "Пост с ID = " + postId + " не был удален");
                });

        // Убираем удаленный пост из списка очистки
        postsToCleanup.get().remove(Integer.valueOf(postId));
    }
}
