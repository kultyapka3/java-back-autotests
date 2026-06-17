package com.company.tests.d1;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Optional;

import io.restassured.response.Response;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import com.company.base.BaseTest;
import com.company.db.PostRecord;
import com.company.models.wp.PostRequest;
import com.company.models.wp.PostResponse;

/** ТК02. Обновление поста */
@Epic("WordPress DB")
@Feature("Управление постами")
@Story("Обновление поста")
@Severity(SeverityLevel.CRITICAL)
public class UpdatePostTest extends BaseTest {

    @Test(
            description = "ТК02. Обновление поста",
            groups = {"wp", "positive", "posts", "d1"})
    public void testUpdatePost() {
        int postId = createTestPost();

        PostRequest updateRequest =
                PostRequest.builder().title("Updated Title").content("Updated Content").build();

        Response apiResponse =
                Allure.step(
                        "Обновление поста", () -> wpApiClient.updatePost(postId, updateRequest));

        int statusCode = apiResponse.getStatusCode();
        PostResponse response = apiResponse.as(PostResponse.class);

        Allure.step(
                "Проверка статуса ответа",
                () -> assertEquals(statusCode, 200, "Ожидался 200 OK, получен " + statusCode));

        Allure.step(
                "Проверка содержимого ответа",
                () -> {
                    assertEquals(
                            response.getTitleText(), "Updated Title", "Заголовок не обновился");
                    assertEquals(
                            response.getContentText(), "Updated Content", "Контент не обновился");
                });

        Allure.step(
                "Проверка обновленного поста в БД",
                () -> {
                    Optional<PostRecord> dbPost = dbClient.getPostById(postId);
                    assertTrue(dbPost.isPresent(), "Пост с ID " + postId + " не найден в БД");
                    assertEquals(
                            dbPost.get().getPostTitle(),
                            "Updated Title",
                            "Пост с ID " + postId + " не был обновлен");
                });
    }
}
