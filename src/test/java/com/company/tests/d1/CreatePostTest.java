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

/** ТК001. Создание поста */
@Epic("WordPress DB")
@Feature("Управление постами")
@Story("Создание поста")
@Severity(SeverityLevel.CRITICAL)
public class CreatePostTest extends BaseTest {

    @Test(
            description = "ТК001. Создание поста",
            groups = {"wp", "positive", "posts", "d1"})
    public void testCreatePost() {
        PostRequest request =
                PostRequest.builder()
                        .title("TestPost1")
                        .status("draft")
                        .content("Test content")
                        .build();

        Response apiResponse = Allure.step("Создание поста", () -> wpApiClient.createPost(request));
        int statusCode = apiResponse.getStatusCode();
        PostResponse response = apiResponse.as(PostResponse.class);

        Allure.step(
                "Проверка статуса ответа",
                () -> {
                    assertEquals(
                            statusCode,
                            201,
                            "Ожидался статус 201 Created, но получен " + statusCode);
                });

        int postId = response.getId();
        postsToCleanup.get().add(postId);

        Allure.step(
                "Проверка содержимого ответа",
                () -> {
                    assertEquals(
                            response.getTitleText(),
                            "TestPost1",
                            "Заголовок в ответе не совпадает");
                    assertEquals(response.getStatus(), "draft", "Статус в ответе не совпадает");
                    assertEquals(
                            response.getContentText(),
                            "Test content",
                            "Контент в ответе не совпадает");
                });

        Allure.step(
                "Проверка содержимого БД",
                () -> {
                    Optional<PostRecord> dbPost = dbClient.getPostById(postId);
                    assertTrue(dbPost.isPresent(), "Пост с ID " + postId + " не найден в БД");
                    assertEquals(
                            dbPost.get().getPostTitle(),
                            "TestPost1",
                            "Заголовок в БД не совпадает");
                });
    }
}
